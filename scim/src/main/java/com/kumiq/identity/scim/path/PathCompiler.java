package com.kumiq.identity.scim.path;

import com.kumiq.identity.scim.filter.FilterCompiler;
import com.kumiq.identity.scim.filter.Predicate;
import com.kumiq.identity.scim.utils.ExceptionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.kumiq.identity.scim.utils.TypeUtils.*;

/**
 * Compiler for a SCIM path. Examples:
 * - foo.bar
 * - foo.bar[value eq 100].foobar
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class PathCompiler {

    private static final Logger log = LoggerFactory.getLogger(PathCompiler.class);

    private final CompilationContext context;
    private final Configuration configuration;

    public static List<PathRef> compile(CompilationContext context, Configuration configuration) {
        return new PathCompiler(context, configuration).doCompile();
    }

    private PathCompiler(CompilationContext context, Configuration configuration) {
        this.configuration = configuration;
        this.context = context;
    }

    /**
     * Compile the SCIM path and return the head for the token list. The
     * list shall only contain {@link SimplePathToken} and {@link PathWithIndexToken}
     *
     * @return head of the path token tree
     */
    private List<PathRef> doCompile() {
        Tokenizer pathTokenizer = new Tokenizer.PathTokenizer(this.context.getPath());
        List<PathToken> tokens = new ArrayList<>();
        tokens.add(PathTokenFactory.root());

        /* form linked list of path tokens, led by the root token */
        while (true) {
            try {
                String tokenValue = pathTokenizer.nextSequence().toString();
                PathToken token = containsFilter(tokenValue) ?
                        PathTokenFactory.pathWithFilter(tokenValue) :
                        PathTokenFactory.simplePathToken(tokenValue);
                tokens.add(token);
            } catch (Tokenizer.NoMoreSequenceException ex) {
                break;
            }
        }
        PathToken.linkTokenList(tokens);

        /* expand filter tokens and replace them with index tokens */
        PathToken pathRoot = tokens.get(0);
        List<PathRef> compiledPathHeads = traverseAndReplace(pathRoot);
        if (CollectionUtils.isEmpty(compiledPathHeads) && !shouldSuppressException()) {
            throw ExceptionFactory.pathCompiledToVoid(this.context.getPath(), this.context.getPath());
        }

        return compiledPathHeads;
    }

    /**
     * In all possible paths of the tree, search for {@link PathWithFilterToken} and replace it with a list
     * of {@link PathWithIndexToken}. In the event when replacement happens, restart the search from the beginning
     * to maintain correct results as context has changed.
     *
     * @param root
     */
    private List<PathRef> traverseAndReplace(PathToken root) {
        List<PathRef> filterFreeRoots = new ArrayList<>();
        Queue<PathToken> traverseBacklog = new LinkedList<>();
        traverseBacklog.add(root);

        while (traverseBacklog.size() > 0) {
            PathToken rootToTraverse = traverseBacklog.poll();
            List<PathRef> pathHeads = rootToTraverse.traverse();

            for (PathRef head : pathHeads) {
                Optional<PathWithFilterToken> result = findFirstPathWithReferenceToken(head);
                if (result.isPresent()) {
                    PathWithFilterToken tokenToReplace = result.get();
                    List<PathWithIndexToken> replacementTokens = expandMultiValuedWithFilter(tokenToReplace);
                    if (CollectionUtils.isEmpty(replacementTokens)) {
                        if (shouldSuppressException()) {
                            log.trace(tokenToReplace.pathFragment() + " evaluated to nothing. This path will be dropped.");
                            continue;
                        } else {
                            throw ExceptionFactory.pathCompiledToVoid(this.context.getPath(), tokenToReplace.pathFragment());
                        }
                    }

                    replacementTokens.sort((o1, o2) -> {
                        if (o1.getIndexComponent() > o2.getIndexComponent())
                            return -1;
                        else if (o1.getIndexComponent() < o2.getIndexComponent())
                            return 1;
                        else
                            return 0;
                    });

                    PathToken prev = tokenToReplace.getPrev();
                    prev.replaceTokens(tokenToReplace, replacementTokens);
                    traverseBacklog.add(head.getPathToken());
                    break;
                } else {
                    filterFreeRoots.add(head);
                }
            }
        }

        return filterFreeRoots;
    }

    private Optional<PathWithFilterToken> findFirstPathWithReferenceToken(PathRef root) {
        PathRef cursor = root;
        while (cursor != null) {
            if (cursor.getPathToken() instanceof PathWithFilterToken)
                return Optional.of((PathWithFilterToken) cursor.getPathToken());
            cursor = cursor.getNext();
        }
        return Optional.empty();
    }

    private List<PathWithIndexToken> expandMultiValuedWithFilter(PathWithFilterToken token) {
        PathRef pathHead = PathRef.createReferenceTo(token.getPrev());

        Optional<PathWithFilterToken> result = findFirstPathWithReferenceToken(pathHead);
        Assert.isTrue(!result.isPresent(), "Cloned path cannot contain another token with filter before the one supplied to evaluate.");

        EvaluationContext evalContext = new EvaluationContext(this.context.getData());
        evalContext = pathHead.evaluate(evalContext, this.configuration);
        Object value = evalContext.getCursor();

        Object array = configuration.getObjectProvider().getPropertyValue(value, token.getPathComponent());
        Assert.isTrue(isList(array));
        List list = asList(array);
        if (list.size() == 0)
            return new ArrayList<>();

        List<Integer> qualifiedIndex = new ArrayList<>();
        Predicate predicate = FilterCompiler.compile(token.getFilterComponent());
        for (int i = 0; i < list.size(); i++) {
            if (predicate.apply(list.get(i), this.configuration)) {
                qualifiedIndex.add(i);
            }
        }

        return qualifiedIndex
                .stream()
                .map(integer -> new PathWithIndexToken(token.getPathComponent() + "[" + integer + "]"))
                .collect(Collectors.toList());
    }

    private boolean containsFilter(String token) {
        return token.contains("[") && token.endsWith("]");
    }

    private boolean shouldSuppressException() {
        return this.configuration.getOptions().contains(Configuration.Option.SUPPRESS_EXCEPTION);
    }
}
