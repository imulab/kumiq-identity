package com.kumiq.identity.scim.task.group.query;

import com.kumiq.identity.scim.resource.group.Group;
import com.kumiq.identity.scim.task.GroupQueryContext;
import com.kumiq.identity.scim.task.Task;
import org.springframework.util.StringUtils;

import java.util.ArrayList;

/**
 * TODO consider merge with {@link com.kumiq.identity.scim.task.user.query.AssignQueryContextTask}
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class AssignQueryContextTask<T extends Group> implements Task<GroupQueryContext<T>> {

    private static final String DEFAULT_FILTER = "id pr";
    private static final String DEFAULT_SORT = "meta.created";
    private static final int DEFAULT_START = 1;
    private static final int DEFAULT_PAGE_SIZE = 20;

    @Override
    public void perform(GroupQueryContext<T> context) {
        if (context.getAttributes() == null)
            context.setAttributes(new ArrayList<>());

        if (!StringUtils.hasLength(context.getFilter()))
            context.setFilter(DEFAULT_FILTER);

        if (!StringUtils.hasLength(context.getSort()))
            context.setSort(DEFAULT_SORT);

        if (context.getStartIndex() <= 0)
            context.setStartIndex(DEFAULT_START);

        if (context.getCount() < 0)
            context.setCount(DEFAULT_PAGE_SIZE);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
