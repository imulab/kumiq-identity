package com.kumiq.identity.scim.path;

import com.kumiq.identity.scim.resource.misc.Schema;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class CompilationContext {

    private final Object data;

    private final String path;

    private Schema schema;

    public static CompilationContext create(String path, Object data) {
        return new CompilationContext(path, data);
    }

    public CompilationContext withSchema(Schema schema) {
        this.schema = schema;
        return this;
    }

    public CompilationContext(String path, Object data) {
        this.path = path;
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public String getPath() {
        return path;
    }

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }
}
