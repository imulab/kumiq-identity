package com.kumiq.identity.scim.task.shared;

import com.kumiq.identity.scim.database.ResourceDatabase;
import com.kumiq.identity.scim.resource.constant.ScimConstants;
import com.kumiq.identity.scim.resource.core.Resource;
import com.kumiq.identity.scim.resource.misc.Schema;
import com.kumiq.identity.scim.task.ResourceOpContext;
import com.kumiq.identity.scim.task.Task;
import com.kumiq.identity.scim.exception.ExceptionFactory;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * Set schema into context to assist operation
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class SetSchemaTask<T extends Resource> implements Task<ResourceOpContext<T>> {

    private ResourceDatabase.SchemaDatabase database;
    private String id;

    @Override
    public void perform(ResourceOpContext<T> context) {
        Optional<Schema> result = database.findById(id);
        if (!result.isPresent())
            throw ExceptionFactory.resourceNotFound(ScimConstants.RESOURCE_TYPE_SCHEMA, id);
        context.setSchema(result.get());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(database);
        Assert.notNull(id);
    }

    public ResourceDatabase.SchemaDatabase getDatabase() {
        return database;
    }

    public void setDatabase(ResourceDatabase.SchemaDatabase database) {
        this.database = database;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
