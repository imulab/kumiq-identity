package com.kumiq.identity.scim.path;

import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.kumiq.identity.scim.utils.TypeUtils.*;

/**
 * A path token with index component after the path
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class PathWithIndexToken extends SimplePathToken {

    private static final Pattern pattern = Pattern.compile("(.*?)\\[(.*?)\\]");

    private final String pathComponent;
    private final Integer indexComponent;

    public PathWithIndexToken(String pathWithIndex) {
        super(pathWithIndex);

        try {
            this.pathComponent = pathWithIndex.split("\\[")[0];
            this.indexComponent = Integer.parseInt(pathWithIndex.split("\\[")[1].split("]")[0]);
        } catch (Exception ex) {
            throw new IllegalArgumentException(pathWithIndex + " is not a valid path with index token");
        }
    }

    @Override
    public Object evaluateSelf(Map<String, Object> cursor) {
        Assert.isTrue(isList(cursor.get(pathComponent)), "Evaluation cannot continue as list cannot be resolved");
        return asList(cursor.get(pathComponent)).get(indexComponent);
    }

    @Override
    public PathEvaluationContext evaluate(Map<String, Object> root, Map<String, Object> cursor) {
        if (cursor.get(pathComponent) == null) {
            return new PathEvaluationContext(root);
        }

        Assert.isTrue(isList(cursor.get(pathComponent)), "Evaluation cannot continue as list cannot be resolved");
        Object value = asList(cursor.get(pathComponent)).get(indexComponent);

        if (!isLeaf()) {
            Assert.isTrue(this.getNext().size() == 1, "Multiple next found. Evaluation should only deal with linked list.");
            Assert.isTrue(isMap(value), "Evaluation cannot continue as map is not the result of evaluation for a non-leaf token.");

            return this.getNext().get(0).evaluate(root, asMap(value));
        } else {
            PathEvaluationContext context = new PathEvaluationContext(root);
            context.setValue(value);
            return context;
        }
    }

    @Override
    public PathToken cloneSelfAndDownStream(PathToken prev) {
        PathWithIndexToken cloned = (PathWithIndexToken) cloneSelfSimple();
        cloned.setPrev(prev);
        if (this.getNext() != null) {
            for (PathToken next : this.getNext()) {
                cloned.appendToken(next.cloneSelfAndDownStream(cloned));
            }
        }
        return cloned;
    }

    @Override
    public PathToken cloneSelfSimple() {
        return new PathWithIndexToken(super.pathFragment());
    }

    public String getPathComponent() {
        return pathComponent;
    }

    public Integer getIndexComponent() {
        return indexComponent;
    }
}
