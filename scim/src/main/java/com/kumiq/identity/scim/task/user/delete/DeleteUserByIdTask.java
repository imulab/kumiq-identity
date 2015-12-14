package com.kumiq.identity.scim.task.user.delete;

import com.kumiq.identity.scim.database.ResourceDatabase;
import com.kumiq.identity.scim.resource.user.User;
import com.kumiq.identity.scim.task.Task;
import com.kumiq.identity.scim.task.UserDeleteContext;
import org.springframework.util.Assert;

/**
 * Delete the user resource
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class DeleteUserByIdTask<T extends User> implements Task<UserDeleteContext<T>> {

    private ResourceDatabase.UserDatabase userDatabase;

    @Override
    public void perform(UserDeleteContext<T> context) {
        userDatabase.delete(context.getId());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(userDatabase);
    }

    public ResourceDatabase.UserDatabase getUserDatabase() {
        return userDatabase;
    }

    public void setUserDatabase(ResourceDatabase.UserDatabase userDatabase) {
        this.userDatabase = userDatabase;
    }
}
