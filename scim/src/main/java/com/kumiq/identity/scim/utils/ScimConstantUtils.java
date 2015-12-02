package com.kumiq.identity.scim.utils;

import static com.kumiq.identity.scim.resource.constant.ScimConstants.*;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class ScimConstantUtils {

    public static boolean isValidAttributeType(String value) {
        return ATTR_TYPES_STRING.equals(value) ||
                ATTR_TYPES_BOOLEAN.equals(value) ||
                ATTR_TYPES_DECIMAL.equals(value) ||
                ATTR_TYPES_INTEGER.equals(value) ||
                ATTR_TYPES_DATETIME.equals(value) ||
                ATTR_TYPES_REFERENCE.equals(value) ||
                ATTR_TYPES_COMPLEX.equals(value);
    }

    public static boolean isValidMutability(String value) {
        return MUTABILITY_READONLY.equals(value) ||
                MUTABILITY_READWRITE.equals(value) ||
                MUTABILITY_IMMUTABLE.equals(value) ||
                MUTABILITY_WRITEONLY.equals(value);
    }

    public static boolean isValidReturned(String value) {
        return RETURNED_ALWAYS.equals(value) ||
                RETURNED_DEFAULT.equals(value) ||
                RETURNED_NEVER.equals(value) ||
                RETURNED_REQUEST.equals(value);
    }

    public static boolean isValidUniqueness(String value) {
        return UNIQUENESS_NONE.equals(value) ||
                UNIQUENESS_GLOBAL.equals(value) ||
                UNIQUENESS_SERVER.equals(value);
    }

    public static boolean isValidReferenceType(String value) {
        return REF_TYPE_SCIM.equals(value) ||
                REF_TYPE_EXTERNAL.equals(value) ||
                REF_TYPE_URI.equals(value) ||
                REF_TYPE_USER.equals(value) ||
                REF_TYPE_GROUP.equals(value);
    }
}
