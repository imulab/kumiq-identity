package com.kumiq.identity.scim.task.user.create;

import com.kumiq.identity.scim.resource.user.User;
import com.kumiq.identity.scim.task.shared.CheckRequiredTask;
import com.kumiq.identity.scim.utils.ExceptionFactory;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class CheckUserRequiredTask<T extends User> extends CheckRequiredTask<T> {

    @Override
    protected ExceptionFactory.ResourceAttributeAbsentException violationException(String path, String resourceId) {
        return ExceptionFactory.userAttributeAbsent(path, resourceId);
    }
}
