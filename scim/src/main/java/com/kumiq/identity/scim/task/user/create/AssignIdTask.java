package com.kumiq.identity.scim.task.user.create;

import com.kumiq.identity.scim.resource.user.User;
import com.kumiq.identity.scim.task.Task;
import com.kumiq.identity.scim.task.UserCreateContext;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class AssignIdTask<T extends User> implements Task<UserCreateContext<T>> {

    @Override
    public void perform(UserCreateContext<T> context) {
        Assert.notNull(context.getResource());
        String id = "user-" + UUID.randomUUID().toString();
        context.getResource().setId(id);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        
    }
}
