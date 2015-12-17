package com.kumiq.identity.scim.task.user.replace;

import com.kumiq.identity.scim.resource.user.User;
import com.kumiq.identity.scim.task.shared.LoadOriginalResourceByIdTask;
import com.kumiq.identity.scim.utils.ExceptionFactory;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class LoadOriginalUserTask<T extends User> extends LoadOriginalResourceByIdTask<T> {

    @Override
    protected ExceptionFactory.ResourceNotFoundException notFoundException(String id) {
        return ExceptionFactory.userResourceNotFound(id);
    }
}
