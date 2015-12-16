package com.kumiq.identity.scim.task.group.create;

import com.kumiq.identity.scim.database.ResourceDatabase;
import com.kumiq.identity.scim.resource.group.Group;
import com.kumiq.identity.scim.task.shared.CheckUniquenessTask;
import com.kumiq.identity.scim.utils.ExceptionFactory;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class CheckGroupUniquenessTask<T extends Group> extends CheckUniquenessTask<T> {

    private ResourceDatabase.GroupDatabase<T> groupDatabase;

    @Override
    protected List<T> performQuery(String query, String sort, boolean ascending) {
        return groupDatabase.query(query, sort, ascending);
    }

    @Override
    protected ExceptionFactory.ResourceUniquenessViolatedException violationException(String path, String resourceId) {
        return ExceptionFactory.groupPathNotUnique(path, resourceId);
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
