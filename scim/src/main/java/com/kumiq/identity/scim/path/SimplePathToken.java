package com.kumiq.identity.scim.path;

import org.springframework.util.Assert;

import java.util.Map;

import static com.kumiq.identity.scim.utils.TypeUtils.*;

/**
 * A simple path token indicating a key on the map.
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class SimplePathToken extends PathToken {

    private final String pathFragment;

    public SimplePathToken(String pathFragment) {
        super();
        this.pathFragment = pathFragment;
    }

    @Override
    public Object evaluateSelf(Map<String, Object> cursor) {
        return cursor.get(pathFragment);
    }

    @Override
    public PathEvaluationContext evaluate(Map<String, Object> root, Map<String, Object> cursor) {
        Object value = cursor.get(pathFragment);

        if (!isLeaf()) {
            if (value == null) {
                return new PathEvaluationContext(root);
            } else {
                Assert.isTrue(this.getNext().size() == 1, "Multiple next found. Evaluation should only deal with linked list.");
                Assert.isTrue(isMap(value), "Evaluation cannot continue as map is not the result of evaluation for a non-leaf token.");
                return this.getNext().get(0).evaluate(root, asMap(value));
            }
        } else {
            PathEvaluationContext context = new PathEvaluationContext(root);
            context.setValue(value);
            return context;
        }
    }

    @Override
    public PathToken cloneSelfAndDownStream(PathToken prev) {
        SimplePathToken cloned = (SimplePathToken) cloneSelfSimple();
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
        return new SimplePathToken(this.pathFragment);
    }

    @Override
    public String pathFragment() {
        return this.pathFragment;
    }

    @Override
    public String queryFreePath() {
        return this.pathFragment;
    }

    public String getPathFragment() {
        return pathFragment;
    }
}
