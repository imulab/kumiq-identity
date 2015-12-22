package com.kumiq.identity.scim.task.user.get;

import com.kumiq.identity.scim.resource.user.User;
import com.kumiq.identity.scim.task.Task;
import com.kumiq.identity.scim.task.UserGetContext;
import com.kumiq.identity.scim.exception.ExceptionFactory;

/**
 * Ensure the user found in previous tasks actually exists (Separated for modularity)
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class AssertUserExistsTask<T extends User> implements Task<UserGetContext<T>> {

    @Override
    public void perform(UserGetContext<T> context) {
        if (context.getResource() == null)
            throw ExceptionFactory.userResourceNotFound(context.getId());
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
