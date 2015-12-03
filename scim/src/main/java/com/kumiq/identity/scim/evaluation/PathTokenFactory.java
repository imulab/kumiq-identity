package com.kumiq.identity.scim.evaluation;

/**
 * Simple factory for {@link PathToken}
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class PathTokenFactory {

    public static PathToken root() {
        return new PathRoot();
    }

    public static PathToken simplePathToken(String path) {
        return new SimplePathToken(path);
    }

    public static PathToken pathWithFilter(String pathWithFilter) {
        return new PathWithFilterToken(pathWithFilter);
    }

    public static PathToken pathWithIndex(String pathWithIndex) {
        return new PathWithIndexToken(pathWithIndex);
    }
}
