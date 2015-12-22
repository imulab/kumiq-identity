package com.kumiq.identity.scim.task.shared;

import com.kumiq.identity.scim.database.ResourceDatabase;
import com.kumiq.identity.scim.resource.core.Resource;
import com.kumiq.identity.scim.task.ReplaceContext;
import com.kumiq.identity.scim.task.Task;
import com.kumiq.identity.scim.exception.ExceptionFactory;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public abstract class LoadOriginalResourceByIdTask<T extends Resource> implements Task<ReplaceContext<T>> {

    private ResourceDatabase<T> database;

    protected abstract ExceptionFactory.ResourceNotFoundException notFoundException(String id);

    @Override
    public void perform(ReplaceContext<T> context) {
        Assert.notNull(context.getId());

        Optional<T> result = database.findById(context.getId());
        if (result.isPresent())
            context.setOriginalCopy(result.get());
        else
            throw notFoundException(context.getId());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(database);
    }

    public ResourceDatabase<T> getDatabase() {
        return database;
    }

    public void setDatabase(ResourceDatabase<T> database) {
        this.database = database;
    }
}
