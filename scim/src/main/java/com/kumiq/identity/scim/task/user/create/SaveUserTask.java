package com.kumiq.identity.scim.task.user.create;

import com.kumiq.identity.scim.database.ResourceDatabase;
import com.kumiq.identity.scim.resource.user.User;
import com.kumiq.identity.scim.task.Task;
import com.kumiq.identity.scim.task.UserCreateContext;
import org.springframework.util.Assert;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class SaveUserTask<T extends User> implements Task<UserCreateContext<T>> {

    private ResourceDatabase.UserDatabase<T> userDatabase;

    @Override
    public void perform(UserCreateContext<T> context) {
        Assert.notNull(context.getResource());

        userDatabase.save(context.getResource());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(userDatabase);
    }

    public ResourceDatabase.UserDatabase<T> getUserDatabase() {
        return userDatabase;
    }

    public void setUserDatabase(ResourceDatabase.UserDatabase<T> userDatabase) {
        this.userDatabase = userDatabase;
    }
}
