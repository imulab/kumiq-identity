package com.kumiq.identity.scim.task.user.delete;

import com.kumiq.identity.scim.resource.user.User;
import com.kumiq.identity.scim.task.shared.CheckVersionTask;
import com.kumiq.identity.scim.utils.ExceptionFactory;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class CheckUserVersionTask<T extends User> extends CheckVersionTask<T> {

    @Override
    protected ExceptionFactory.ResourceNotFoundException notFoundException(String id) {
        return ExceptionFactory.userResourceNotFound(id);
    }

    @Override
    protected ExceptionFactory.ResourceVersionMismatchException versionMismatchException(String id, String accessETag, String actualETag) {
        return ExceptionFactory.userResourceVersionMismatch(id, accessETag, actualETag);
    }
}
