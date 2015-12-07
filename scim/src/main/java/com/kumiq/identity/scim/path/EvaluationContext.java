package com.kumiq.identity.scim.path;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class EvaluationContext {

    private final Object data;

    private Object cursor;

    public EvaluationContext(final Object data) {
        this.data = data;
        this.cursor = data;
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
