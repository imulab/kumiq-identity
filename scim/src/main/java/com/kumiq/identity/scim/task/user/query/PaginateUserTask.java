package com.kumiq.identity.scim.task.user.query;

import com.kumiq.identity.scim.resource.user.User;
import com.kumiq.identity.scim.task.Task;
import com.kumiq.identity.scim.task.UserQueryContext;
import com.kumiq.identity.scim.utils.PaginationUtils;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class PaginateUserTask<T extends User> implements Task<UserQueryContext<T>> {

    @Override
    public void perform(UserQueryContext<T> context) {
        context.setResources(PaginationUtils.subList(context.getResources(), context.getStartIndex(), context.getCount()));
        context.getResults().put(UserQueryContext.START_INDEX, context.getStartIndex());
        context.getResults().put(UserQueryContext.PAGE_COUNT, context.getCount());
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
