package com.kumiq.identity.scim.path;

import com.kumiq.identity.scim.resource.misc.Schema;
import com.kumiq.identity.scim.utils.TypeUtils;
import org.springframework.util.Assert;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class PathRef {

    private final PathToken pathToken;
    private PathRef prev;
    private PathRef next;

    public static PathRef linkList(List<PathRef> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            list.get(i).append(list.get(i + 1));
        }
        return list.get(0).getHead();
    }

    /**
     * Wrap a path token list in path reference list. Assume {@link PathToken#firstNext()} when
     * traversing the path list. The reference list will stop at {@code lastToken}
     *
     * @param lastToken
     * @return
     */
    public static PathRef createReferenceTo(PathToken lastToken) {
        PathToken cursor = lastToken.getRoot();

        List<PathToken> tokens = new ArrayList<>();
        while (cursor != lastToken.firstNext()) {
            tokens.add(cursor);
            cursor = cursor.firstNext();
        }

        List<PathRef> referenceList = tokens
                .stream()
                .map(PathRef::new)
                .collect(Collectors.toList());
        return PathRef.linkList(referenceList);
    }

    public PathRef(PathToken token) {
        this.pathToken = token;
    }

    public PathRef getHead() {
        if (this.isHead())
            return this;

        PathRef cursor = this;
        do {
            cursor = cursor.getPrev();
        } while (!cursor.isHead());
        return cursor;
    }

    public void append(PathRef that) {
        that.setPrev(this);
        this.setNext(that);
    }

    public void modify(ModificationContext context, Configuration configuration) {
        if (!this.isTail()) {
            context.setCursor(this.pathToken.evaluate(context.getCursor(), configuration));
            this.next.modify(context, configuration);
        } else {
            // TODO do actual modify work on last node
        }
    }

    public EvaluationContext evaluate(EvaluationContext context, Configuration configuration) {
        context.setCursor(this.pathToken.evaluate(context.getCursor(), configuration));
        if (this.isTail())
            return context;

        return this.next.evaluate(context, configuration);
    }

    public boolean isHead() {
        return this.prev == null;
    }

    public boolean isTail() {
        return this.next == null;
    }

    public Schema.Attribute getAttribute(Schema schema) {
        PathRef cursor = this.getHead();
        List<PathRef> tokens = new ArrayList<>();
        while (true) {
            if (!(cursor.getPathToken() instanceof PathRoot))
                tokens.add(cursor);
            if (cursor == this)
                break;
            cursor = cursor.getNext();
        }
        List<String> paths = tokens.stream().map(pathRef -> pathRef.getPathToken().queryFreePath()).collect(Collectors.toList());
        if (paths.size() == 0)
            return null;

        Optional<Schema.Attribute> firstLevelResult = schema.getAttributes()
                .stream()
                .filter(attribute -> attribute.getName().equals(paths.get(0)))
                .findFirst();
        if (!firstLevelResult.isPresent())
            return null;

        Schema.Attribute cursorAttr = firstLevelResult.get();
        for (int i = 1; i < paths.size(); i++) {
            final String path = paths.get(i);
            Optional<Schema.Attribute> found = cursorAttr.getSubAttributes()
                    .stream()
                    .filter(attribute -> attribute.getName().equals(path))
                    .findFirst();
            if (!found.isPresent())
                return null;
            else
                cursorAttr = found.get();
        }
        return cursorAttr;
    }

    public PathToken getPathToken() {
        return pathToken;
    }

    public PathRef getPrev() {
        return prev;
    }

    public void setPrev(PathRef prev) {
        this.prev = prev;
    }

    public PathRef getNext() {
        return next;
    }

    public void setNext(PathRef next) {
        this.next = next;
    }
}
