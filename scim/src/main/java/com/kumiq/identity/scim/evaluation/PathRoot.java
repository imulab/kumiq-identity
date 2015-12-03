package com.kumiq.identity.scim.evaluation;

import org.springframework.util.Assert;

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
    public EvaluationContext evaluate(Map<String, Object> root, Map<String, Object> cursor) {
        Assert.isTrue(this.getNext().size() == 1, "Evaluation should only deal with linked list (exactly 1 next token).");
        Assert.isTrue(isRoot() && !isLeaf(), "Root token cannot be leaf.");
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
