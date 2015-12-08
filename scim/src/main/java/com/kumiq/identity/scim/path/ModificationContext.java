package com.kumiq.identity.scim.path;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class ModificationContext {

    private final ModificationUnit mod;

    private final Object data;

    private Object cursor;

    public ModificationContext(ModificationUnit mod, Object data) {
        this.mod = mod;
        this.data = data;
        this.cursor = data;
    }

    public ModificationUnit getMod() {
        return mod;
    }

    public Object getData() {
        return data;
    }

    public Object getCursor() {
        return cursor;
    }

    public void setCursor(Object cursor) {
        this.cursor = cursor;
    }
}
