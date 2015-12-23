package com.kumiq.identity.scim.path;

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

    /**
     * Data to convert to map
     */
    private final Object data;

    /**
     * Internal schema describing all fields in resource. Could be different than the one defined and returned in SCIM API.
     */
    private final Schema schema;

    /**
     * SCIM paths to include
     */
    private List<String> includePaths;

    /**
     * SCIM paths to exclude
     */
    private List<String> excludePaths;

    public MappingContext(Object data, Schema schema, List<String> includePaths, List<String> excludePaths) {
        this.data = data;
        this.schema = schema;
        this.includePaths = !CollectionUtils.isEmpty(includePaths) ? includePaths : defaultPaths();
        this.excludePaths = !CollectionUtils.isEmpty(excludePaths) ? excludePaths : new ArrayList<>();

        Assert.isTrue(data != null, "Data is not set");
        Assert.isTrue(schema != null, "Schema is not set");
        Assert.isTrue(!CollectionUtils.isEmpty(this.includePaths), "Include paths are not set");

        updatePaths();
    }

    private void updatePaths() {
        List<String> paths = this.schema.findAllPaths();
        paths.addAll(this.includePaths);
        paths.addAll(this.excludePaths);
        List<String> alwaysPaths = new ArrayList<>();
        List<String> neverPaths = new ArrayList<>();
        for (String path : paths) {
            Schema.Attribute attribute = this.schema.findAttributeByPath(path);
            if (attribute == null) {
                neverPaths.add(path);
                continue;
            }

            if (ScimConstants.RETURNED_ALWAYS.equals(attribute.getReturned()))
                alwaysPaths.add(path);
            else if (ScimConstants.RETURNED_NEVER.equals(attribute.getReturned()))
                neverPaths.add(path);
        }

        alwaysPaths.forEach(s -> {
            if (!this.includePaths.contains(s))
                this.includePaths.add(s);
            if (this.excludePaths.contains(s))
                this.excludePaths.remove(s);
        });
        neverPaths.forEach(s -> {
            if (this.includePaths.contains(s))
                this.includePaths.remove(s);
            if (!this.excludePaths.contains(s))
                this.excludePaths.add(s);
        });
    }

    private List<String> defaultPaths() {
        List<String> paths = new ArrayList<>();
        schema.getAttributes().forEach(attribute -> paths.add(attribute.getName()));
        return paths;
    }

    public Object getData() {
        return data;
    }

    public Schema getSchema() {
        return schema;
    }

    public List<String> getIncludePaths() {
        return includePaths;
    }

    public List<String> getExcludePaths() {
        return excludePaths;
    }
}
