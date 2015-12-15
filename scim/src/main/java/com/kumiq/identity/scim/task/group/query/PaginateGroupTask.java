package com.kumiq.identity.scim.task.group.query;

import com.kumiq.identity.scim.resource.group.Group;
import com.kumiq.identity.scim.task.GroupQueryContext;
import com.kumiq.identity.scim.task.Task;
import com.kumiq.identity.scim.task.UserQueryContext;
import com.kumiq.identity.scim.utils.PaginationUtils;

/**
 * TODO consider merge with {@link com.kumiq.identity.scim.task.user.query.PaginateUserTask}
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class PaginateGroupTask<T extends Group> implements Task<GroupQueryContext<T>> {

    @Override
    public void perform(GroupQueryContext<T> context) {
        context.setResources(PaginationUtils.subList(context.getResources(), context.getStartIndex(), context.getCount()));
        context.getResults().put(UserQueryContext.START_INDEX, context.getStartIndex());
        context.getResults().put(UserQueryContext.PAGE_COUNT, context.getCount());
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
