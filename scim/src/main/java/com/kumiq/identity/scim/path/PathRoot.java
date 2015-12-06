package com.kumiq.identity.scim.path;

import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * A symbolic root attached to every path list.
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class PathRoot extends PathToken {

    PathRoot() {
        super();
    }

    @Override
    public String pathFragment() {
        return "";
    }

    @Override
    public String queryFreePath() {
        return "";
    }

    @Override
    public Object evaluateSelf(Map<String, Object> cursor) {
        return cursor;
    }

    @Override
    public PathEvaluationContext evaluate(Map<String, Object> root, Map<String, Object> cursor) {
        if (CollectionUtils.isEmpty(this.getNext())) {
            PathEvaluationContext context = new PathEvaluationContext(root);
            context.setValue(cursor);
            return context;
        }

        return this.getNext().get(0).evaluate(root, cursor);
    }

    @Override
    public PathToken cloneSelfAndDownStream(PathToken prev) {
        return new PathRoot();
    }

    @Override
    public PathToken cloneSelfSimple() {
        return new PathRoot();
    }
}
