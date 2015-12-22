package com.kumiq.identity.scim.task.group.delete;

import com.kumiq.identity.scim.resource.group.Group;
import com.kumiq.identity.scim.task.shared.CheckVersionTask;
import com.kumiq.identity.scim.exception.ExceptionFactory;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class CheckGroupVersionTask<T extends Group> extends CheckVersionTask<T> {

    @Override
    protected ExceptionFactory.ResourceNotFoundException notFoundException(String id) {
        return ExceptionFactory.groupResourceNotFound(id);
    }

    @Override
    protected ExceptionFactory.ResourceVersionMismatchException versionMismatchException(String id, String accessETag, String actualETag) {
        return ExceptionFactory.groupResourceVersionMismatch(id, accessETag, actualETag);
    }
}
