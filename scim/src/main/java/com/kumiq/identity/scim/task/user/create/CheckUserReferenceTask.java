package com.kumiq.identity.scim.task.user.create;

import com.kumiq.identity.scim.resource.user.User;
import com.kumiq.identity.scim.task.shared.CheckReferenceTask;
import com.kumiq.identity.scim.exception.ExceptionFactory;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class CheckUserReferenceTask<T extends User> extends CheckReferenceTask<T> {

    @Override
    protected ExceptionFactory.ResourceReferenceViolatedException violationException(String path, String resourceId) {
        return ExceptionFactory.userReferenceViolated(path, resourceId);
    }
}
