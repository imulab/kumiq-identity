package com.kumiq.identity.scim.path;

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
    public Object evaluate(Object cursor, Configuration configuration) {
        return configuration.getObjectProvider().getPropertyValue(cursor, this.pathFragment);
    }

    @Override
    public String pathFragment() {
        return this.pathFragment;
    }

    @Override
    public String queryFreePath() {
        return this.pathFragment;
    }

    @Override
    public boolean isSimplePath() {
        return true;
    }

    public String getPathFragment() {
        return pathFragment;
    }
}
