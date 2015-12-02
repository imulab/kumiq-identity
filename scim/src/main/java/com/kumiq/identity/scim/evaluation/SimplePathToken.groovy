package com.kumiq.identity.scim.evaluation

/**
 * A simple path token indicating a key on the map.
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
class SimplePathToken extends PathToken {

    private final String pathFragment;

    public SimplePathToken(String pathFragment) {
        super();
        this.pathFragment = pathFragment;
    }

    @Override
    String pathFragment() {
        this.pathFragment;
    }

    String getPathFragment() {
        return pathFragment
    }
}
