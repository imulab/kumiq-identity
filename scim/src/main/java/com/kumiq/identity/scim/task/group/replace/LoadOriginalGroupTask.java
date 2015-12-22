package com.kumiq.identity.scim.task.group.replace;

import com.kumiq.identity.scim.resource.group.Group;
import com.kumiq.identity.scim.task.shared.LoadOriginalResourceByIdTask;
import com.kumiq.identity.scim.exception.ExceptionFactory;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class LoadOriginalGroupTask<T extends Group> extends LoadOriginalResourceByIdTask<T> {

    @Override
    protected ExceptionFactory.ResourceNotFoundException notFoundException(String id) {
        return ExceptionFactory.groupResourceNotFound(id);
    }
}
