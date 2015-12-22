package com.kumiq.identity.scim.task.group.create;

import com.kumiq.identity.scim.resource.group.Group;
import com.kumiq.identity.scim.task.shared.CheckRequiredTask;
import com.kumiq.identity.scim.exception.ExceptionFactory;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class CheckGroupRequiredTask<T extends Group> extends CheckRequiredTask<T> {

    @Override
    protected ExceptionFactory.ResourceAttributeAbsentException violationException(String path, String resourceId) {
        return ExceptionFactory.groupAttributeAbsent(path, resourceId);
    }
}
