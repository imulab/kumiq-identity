package com.kumiq.identity.scim.mapper;

import com.kumiq.identity.scim.path.Configuration;

import java.util.Map;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public interface ResourceMapper {

    /**
     * Converts the given object to Map, with the help of schema and path inclusion / exclusion hints.
     *
     * @param context
     * @param configuration
     * @return
     */
    Map<String, Object> convertToMap(MappingContext context, Configuration configuration);
}
