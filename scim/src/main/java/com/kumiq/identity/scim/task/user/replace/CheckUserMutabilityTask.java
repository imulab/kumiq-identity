package com.kumiq.identity.scim.task.user.replace;

import com.kumiq.identity.scim.resource.user.User;
import com.kumiq.identity.scim.task.shared.CheckMutabilityTask;
import com.kumiq.identity.scim.utils.ExceptionFactory;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class CheckUserMutabilityTask<T extends User> extends CheckMutabilityTask<T> {

    @Override
    protected ExceptionFactory.ResourceImmutabilityViolatedException violatedException(String path, String resourceId) {
        return ExceptionFactory.userImmutabilityViolated(path, resourceId);
    }
}
