package com.kumiq.identity.scim.path;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class CompilationContext {

    private final Object data;

    private final String path;

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
}
