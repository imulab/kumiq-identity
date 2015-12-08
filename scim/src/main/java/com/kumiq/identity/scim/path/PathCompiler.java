package com.kumiq.identity.scim.path;

import com.kumiq.identity.scim.filter.FilterCompiler;
import com.kumiq.identity.scim.filter.Predicate;
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

    public static List<PathRef> compile(String path, Map<String, Object> data) {
        return new PathCompiler().doCompile(path, data);
    }

    /**
     * Compile the SCIM path and return the head for the token list. The
     * list shall only contain {@link SimplePathToken} and {@link PathWithIndexToken}
     *
     * @param path SCIM path
     * @param data the object used for index evaluation
     * @return head of the path token tree
     */
    private List<PathRef> doCompile(String path, Map<String, Object> data) {
        Tokenizer pathTokenizer = new Tokenizer.PathTokenizer(path);
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
        return traverseAndReplace(pathRoot, data);
    }

    /**
     * In all possible paths of the tree, search for {@link PathWithFilterToken} and replace it with a list
     * of {@link PathWithIndexToken}. In the event when replacement happens, restart the search from the beginning
     * to maintain correct results as context has changed.
     *
     * @param root
     */
    private List<PathRef> traverseAndReplace(PathToken root, Map<String, Object> data) {
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
                    List<PathWithIndexToken> replacementTokens = resolvePathWithFilterToken(tokenToReplace, data);
                    // TODO sort replacement tokens by index from high to low
                    if (CollectionUtils.isEmpty(replacementTokens)) {
                        throw new FilterTokenResolvedToNothingException(tokenToReplace);
                    }

                    PathToken prev = tokenToReplace.getPrev();
                    prev.replaceTokens(tokenToReplace, replacementTokens);
                    //prev.getNext().forEach(PathToken::replaceDownstreamWithClones);

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

    private List<PathWithIndexToken> resolvePathWithFilterToken(PathWithFilterToken token, Map<String, Object> data) {
        PathRef pathHead = PathRef.createReferenceTo(token.getPrev());

        Optional<PathWithFilterToken> result = findFirstPathWithReferenceToken(pathHead);
        Assert.isTrue(!result.isPresent(), "Cloned path cannot contain another token with filter before the one supplied to evaluate.");

        EvaluationContext context = new EvaluationContext(data);
        context = pathHead.evaluate(context, Configuration.withMapObjectProvider());
        Object value = context.getCursor();

        Assert.isTrue(isMap(value));
        Assert.isTrue(isList(asMap(value).get(token.getPathComponent())));
        List list = asList(asMap(value).get(token.getPathComponent()));
        if (list.size() == 0)
            return new ArrayList<>();

        List<Integer> qualifiedIndex = new ArrayList<>();
        Predicate predicate = FilterCompiler.compile(token.getFilterComponent());
        for (int i = 0; i < list.size(); i++) {
            if (isMap(list.get(i))) {
                if (predicate.apply(asMap(list.get(i)))) {
                    qualifiedIndex.add(i);
                }
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

    /**
     * Exception thrown when a {@link PathWithFilterToken} didn't resolve to any {@link PathWithIndexToken}, indicating
     * nothing can be supplied for downstream tokens. Callers may decide what to do with the exception.
     */
    public static class FilterTokenResolvedToNothingException extends RuntimeException {

        private final PathWithFilterToken token;

        FilterTokenResolvedToNothingException(PathWithFilterToken token) {
            this.token = token;
        }

        public PathWithFilterToken getToken() {
            return token;
        }
    }
}
