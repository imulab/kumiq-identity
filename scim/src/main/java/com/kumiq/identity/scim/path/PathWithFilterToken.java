package com.kumiq.identity.scim.path;

import java.util.Map;

/**
 * A path token with filter component after the path
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class PathWithFilterToken extends SimplePathToken {

    private final String pathComponent;
    private final String filterComponent;

    public PathWithFilterToken(String pathWithFilter) {
        super(pathWithFilter);
        try {
            this.pathComponent = pathWithFilter.split("\\[")[0];
            this.filterComponent = pathWithFilter.split("\\[")[1].split("]")[0];
        } catch (Exception ex) {
            throw new IllegalArgumentException(pathWithFilter + " is not a valid path with filter token");
        }
    }

    @Override
    public PathEvaluationContext evaluate(Map<String, Object> root, Map<String, Object> cursor) {
        throw new RuntimeException("PathWithFilterToken does not support evaluation. Compile it to PathWithIndexToken instead.");
    }

    @Override
    public PathToken cloneSelfAndDownStream(PathToken prev) {
        PathWithFilterToken cloned = (PathWithFilterToken) cloneSelfSimple();
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
        return new PathWithFilterToken(super.pathFragment());
    }

    public String getPathComponent() {
        return pathComponent;
    }

    public String getFilterComponent() {
        return filterComponent;
    }
}
