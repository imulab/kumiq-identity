package com.kumiq.identity.scim.task.group.create;

import com.kumiq.identity.scim.resource.group.Group;
import com.kumiq.identity.scim.task.shared.CheckReferenceTask;
import com.kumiq.identity.scim.exception.ExceptionFactory;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class CheckGroupReferenceTask<T extends Group> extends CheckReferenceTask<T> {

    @Override
    protected ExceptionFactory.ResourceReferenceViolatedException violationException(String path, String resourceId) {
        return ExceptionFactory.groupReferenceViolated(path, resourceId);
    }
}
