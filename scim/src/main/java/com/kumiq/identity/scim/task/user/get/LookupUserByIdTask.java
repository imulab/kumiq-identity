package com.kumiq.identity.scim.task.user.get;

import com.kumiq.identity.scim.database.ResourceDatabase;
import com.kumiq.identity.scim.resource.user.User;
import com.kumiq.identity.scim.task.Task;
import com.kumiq.identity.scim.task.UserGetContext;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * Lookup the user resource by id from database
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class LookupUserByIdTask<T extends User> implements Task<UserGetContext<T>> {

    private ResourceDatabase.UserDatabase<T> database;

    @Override
    public void perform(UserGetContext<T> context) {
        Assert.notNull(context.getId());

        Optional<T> result = database.findById(context.getId());
        if (result.isPresent())
            context.setResource(result.get());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(database);
    }

    public ResourceDatabase.UserDatabase<T> getDatabase() {
        return database;
    }

    public void setDatabase(ResourceDatabase.UserDatabase<T> database) {
        this.database = database;
    }
}
