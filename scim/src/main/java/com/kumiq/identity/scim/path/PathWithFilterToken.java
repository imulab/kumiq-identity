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
    public Object evaluate(Object cursor, Configuration configuration) {
        throw new RuntimeException("PathWithFilterToken does not support evaluation. Compile it to PathWithIndexToken instead.");
    }

    public String getPathComponent() {
        return pathComponent;
    }

    @Override
    public boolean isPathWithFilter() {
        return true;
    }

    @Override
    public boolean isSimplePath() {
        return false;
    }

    @Override
    public String queryFreePath() {
        return this.getPathComponent();
    }

    public String getFilterComponent() {
        return filterComponent;
    }
}
