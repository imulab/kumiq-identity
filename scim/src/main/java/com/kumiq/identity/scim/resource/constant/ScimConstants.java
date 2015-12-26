package com.kumiq.identity.scim.resource.constant;

/**
 * @author Weinan Qiu
 * @since 0.1.0
 */
public class ScimConstants {

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static final String SCIM_CONTENT_TYPE = "application/json+scim";
    public static final String ORDER_ASC = "ascending";
    public static final String DEFAULT_FILTER = "id pr";
    public static final String DEFAULT_SORT_BY = "meta.created";
    public static final String DEFAULT_ATTR = "";
    public static final String DEFAULT_START_IDX = "1";
    public static final String DEFAULT_COUNT = "20";
    public static final String SCHEMAS = "schemas";
    public static final String HINT_USER = "HintUser";
    public static final String HINT_GROUP = "HintGroup";

    public static final String RESOURCE_TYPE_USER = "User";
    public static final String RESOURCE_TYPE_GROUP = "Group";
    public static final String RESOURCE_TYPE_SERVICE_PROVIDER_CONFIG = "ServiceProviderConfig";
    public static final String RESOURCE_TYPE_RESOURCE_TYPE = "ResourceType";
    public static final String RESOURCE_TYPE_SCHEMA = "Schema";
    public static final String RESOURCE_TYPE_UNKNOWN = "Unknown";

    public static final String URN_USER = "urn:ietf:params:scim:schemas:core:2.0:User";
    public static final String URN_ENTERPRISE_USER_EXTENSION = "urn:ietf:params:scim:schemas:extension:enterprise:2.0:User";
    public static final String URN_GROUP = "urn:ietf:params:scim:schemas:core:2.0:Group";
    public static final String URN_SERVICE_PROVIDER_CONFIG = "urn:ietf:params:scim:schemas:core:2.0:ServiceProviderConfig";
    public static final String URN_RESOURCE_TYPE = "urn:ietf:params:scim:schemas:core:2.0:ResourceType";
    public static final String URN_SCHEMA = "urn:ietf:params:scim:schemas:core:2.0:Schema";
    public static final String URN_LIST_RESPONSE = "urn:ietf:params:scim:api:messages:2.0:ListResponse";
    public static final String URN_BULK_REQUEST = "urn:ietf:params:scim:api:messages:2.0:BulkRequest";
    public static final String URN_BULK_RESPONSE = "urn:ietf:params:scim:api:messages:2.0:BulkResponse";

    public static final String ATTR_TYPES_STRING = "string";
    public static final String ATTR_TYPES_BOOLEAN = "boolean";
    public static final String ATTR_TYPES_DECIMAL = "decimal";
    public static final String ATTR_TYPES_INTEGER = "integer";
    public static final String ATTR_TYPES_DATETIME = "datetime";
    public static final String ATTR_TYPES_REFERENCE = "reference";
    public static final String ATTR_TYPES_COMPLEX = "complex";

    public static final String MUTABILITY_READONLY = "readOnly";
    public static final String MUTABILITY_READWRITE = "readWrite";
    public static final String MUTABILITY_IMMUTABLE = "immutable";
    public static final String MUTABILITY_WRITEONLY = "writeOnly";

    public static final String RETURNED_ALWAYS = "always";
    public static final String RETURNED_NEVER = "never";
    public static final String RETURNED_DEFAULT = "default";
    public static final String RETURNED_REQUEST = "request";

    public static final String UNIQUENESS_NONE = "none";
    public static final String UNIQUENESS_SERVER = "server";
    public static final String UNIQUENESS_GLOBAL = "global";

    public static final String REF_TYPE_SCIM = "scim";
    public static final String REF_TYPE_EXTERNAL = "external";
    public static final String REF_TYPE_URI = "uri";
    public static final String REF_TYPE_USER = "User";
    public static final String REF_TYPE_GROUP = "Group";

    public static final String FILTER_LEFT_BRACKET = "(";
    public static final String FILTER_RIGHT_BRACKET = ")";
    public static final String FILTER_AND = "and";
    public static final String FILTER_OR = "or";
    public static final String FILTER_NOT = "not";
    public static final String FILTER_EQUAL = "eq";
    public static final String FILTER_NOT_EQUAL = "ne";
    public static final String FILTER_CONTAINS = "co";
    public static final String FILTER_STARTS_WITH= "sw";
    public static final String FILTER_ENDS_WITH = "ew";
    public static final String FILTER_PRESENT = "pr";
    public static final String FILTER_GREATER_THAN = "gt";
    public static final String FILTER_GREATER_EQUAL = "ge";
    public static final String FILTER_LESS_THAN = "lt";
    public static final String FILTER_LESS_EQUAL = "le";

    public static final String GROUP_TYPE_DIRECT = "direct";
    public static final String GROUP_TYPE_INDIRECT = "indirect";

}
