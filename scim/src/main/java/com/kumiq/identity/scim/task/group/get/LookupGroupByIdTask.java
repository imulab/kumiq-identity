package com.kumiq.identity.scim.task.group.get;

import com.kumiq.identity.scim.database.ResourceDatabase;
import com.kumiq.identity.scim.resource.group.Group;
import com.kumiq.identity.scim.task.GroupGetContext;
import com.kumiq.identity.scim.task.Task;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * Lookup the group resource by id from database
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class LookupGroupByIdTask<T extends Group> implements Task<GroupGetContext<T>> {

    private ResourceDatabase.GroupDatabase<T> database;

    @Override
    public void perform(GroupGetContext<T> context) {
        Assert.notNull(context.getId());

        Optional<T> result = database.findById(context.getId());
        if (result.isPresent())
            context.setResource(result.get());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(database);
    }

    public ResourceDatabase.GroupDatabase<T> getDatabase() {
        return database;
    }

    public void setDatabase(ResourceDatabase.GroupDatabase<T> database) {
        this.database = database;
    }
}
