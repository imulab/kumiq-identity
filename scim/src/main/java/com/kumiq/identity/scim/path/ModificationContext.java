package com.kumiq.identity.scim.path;

import com.kumiq.identity.scim.resource.misc.Schema;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class ModificationContext {

    private final ModificationUnit mod;

    private final Schema schema;

    private final Object data;

    private Object cursor;

    public ModificationContext(ModificationUnit mod, Schema schema, Object data) {
        this.mod = mod;
        this.data = data;
        this.cursor = data;
        this.schema = schema;
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

    public Schema getSchema() {
        return schema;
    }
}
