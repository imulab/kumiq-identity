package com.kumiq.identity.scim.task.group.create;

import com.kumiq.identity.scim.database.ResourceDatabase;
import com.kumiq.identity.scim.resource.group.Group;
import com.kumiq.identity.scim.task.shared.SaveResourceTask;
import org.springframework.util.Assert;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class SaveGroupTask<T extends Group> extends SaveResourceTask<T> {

    private ResourceDatabase.GroupDatabase<T> groupDatabase;

    @Override
    protected ResourceDatabase<T> resourceDatabase() {
        return this.groupDatabase;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(groupDatabase);
    }

    public ResourceDatabase.GroupDatabase<T> getGroupDatabase() {
        return groupDatabase;
    }

    public void setGroupDatabase(ResourceDatabase.GroupDatabase<T> groupDatabase) {
        this.groupDatabase = groupDatabase;
    }
}
