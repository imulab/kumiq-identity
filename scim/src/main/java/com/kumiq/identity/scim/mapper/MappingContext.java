package com.kumiq.identity.scim.mapper;

import com.kumiq.identity.scim.resource.constant.ScimConstants;
import com.kumiq.identity.scim.resource.misc.Schema;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class MappingContext {

    private final Object data;

    private final List<Schema> schemas;

    private List<String> includePaths;

    private List<String> excludePaths;

    public MappingContext(Object data, List<Schema> schemas, List<String> includePaths, List<String> excludePaths) {
        this.data = data;
        this.schemas = schemas;
        this.includePaths = !CollectionUtils.isEmpty(includePaths) ? includePaths : defaultPaths();
        this.excludePaths = !CollectionUtils.isEmpty(excludePaths) ? excludePaths : new ArrayList<>();

        Assert.isTrue(data != null, "Data is not set");
        Assert.isTrue(!CollectionUtils.isEmpty(schemas), "Schema is not set");
        Assert.isTrue(!CollectionUtils.isEmpty(this.includePaths), "Include paths are not set");

        updateIncludePaths();
        updateExcludePaths();
    }

    private List<String> defaultPaths() {
        Assert.isTrue(!CollectionUtils.isEmpty(schemas), "Schema is not set");
        List<String> paths = new ArrayList<>();
        this.schemas.forEach(schema -> schema.getAttributes().forEach(attribute -> paths.add(attribute.getName())));
        return paths;
    }

    private void updateIncludePaths() {
        List<String> pathsToRemove = new ArrayList<>();
        for (String eachPath : this.includePaths) {
            Schema.Attribute attribute = findAttribute(eachPath);

            if (attribute == null) {
                pathsToRemove.add(eachPath);
                continue;
            }

            if (ScimConstants.RETURNED_NEVER.equals(attribute.getReturned())) {
                pathsToRemove.add(eachPath);
                continue;
            }
        }
        includePaths.removeAll(pathsToRemove);
    }

    private void updateExcludePaths() {
        List<String> pathsToRemove = new ArrayList<>();
        for (String eachPath : this.excludePaths) {
            Schema.Attribute attribute = findAttribute(eachPath);

            if (attribute == null) {
                pathsToRemove.add(eachPath);
                continue;
            }

            if (ScimConstants.RETURNED_ALWAYS.equals(attribute.getReturned())) {
                pathsToRemove.add(eachPath);
                continue;
            }
        }
        excludePaths.removeAll(pathsToRemove);
    }

    private Schema.Attribute findAttribute(String path) {
        Schema.Attribute attribute;
        for (Schema schema : schemas) {
            attribute = schema.findAttributeByPath(path);
            if (attribute != null)
                return attribute;
        }
        return null;
    }

    public Object getData() {
        return data;
    }

    public List<Schema> getSchemas() {
        return schemas;
    }

    public List<String> getIncludePaths() {
        return includePaths;
    }

    public List<String> getExcludePaths() {
        return excludePaths;
    }
}
