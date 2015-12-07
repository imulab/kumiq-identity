package com.kumiq.identity.scim.path;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A unit to modify resources
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class ModificationUnit {

    @JsonProperty("op")
    private Operation operation;

    @JsonProperty("path")
    private String path;

    @JsonProperty("value")
    private Object value;

    public ModificationUnit() {
    }

    public ModificationUnit(Operation operation, String path, Object value) {
        this.operation = operation;
        this.path = path;
        this.value = value;
    }

    public static enum Operation {
        ADD("add"), REPLACE("replace"), REMOVE("remove");

        private final String value;

        private Operation(String value) {
            this.value = value;
        }

        public static Operation fromString(String value) {
            if (ADD.value.equals(value))
                return ADD;
            else if (REPLACE.value.equals(value))
                return REPLACE;
            else if (REMOVE.value.equals(value))
                return REMOVE;
            else
                throw new IllegalArgumentException(value + " cannot be parsed as ModificationUnit.Operation");
        }

        public String getValue() {
            return value;
        }
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
