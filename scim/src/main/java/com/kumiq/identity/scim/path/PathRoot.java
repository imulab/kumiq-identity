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
    public Object evaluate(Object cursor, Configuration configuration) {
        return cursor;
    }
}
