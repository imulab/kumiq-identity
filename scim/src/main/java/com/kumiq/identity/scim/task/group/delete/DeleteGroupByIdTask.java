package com.kumiq.identity.scim.task.group.delete;

import com.kumiq.identity.scim.database.ResourceDatabase;
import com.kumiq.identity.scim.resource.group.Group;
import com.kumiq.identity.scim.task.GroupDeleteContext;
import com.kumiq.identity.scim.task.Task;
import org.springframework.util.Assert;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class DeleteGroupByIdTask<T extends Group> implements Task<GroupDeleteContext<T>> {

    private ResourceDatabase.GroupDatabase groupDatabase;

    @Override
    public void perform(GroupDeleteContext<T> context) {
        groupDatabase.delete(context.getId());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(groupDatabase);
    }

    public ResourceDatabase.GroupDatabase getGroupDatabase() {
        return groupDatabase;
    }

    public void setGroupDatabase(ResourceDatabase.GroupDatabase groupDatabase) {
        this.groupDatabase = groupDatabase;
    }
}
