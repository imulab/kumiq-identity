package com.kumiq.identity.scim.task.group.query;

import com.kumiq.identity.scim.database.ResourceDatabase;
import com.kumiq.identity.scim.resource.group.Group;
import com.kumiq.identity.scim.task.GroupQueryContext;
import com.kumiq.identity.scim.task.Task;
import com.kumiq.identity.scim.utils.ExceptionFactory;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class QueryGroupTask<T extends Group> implements Task<GroupQueryContext<T>> {

    private ResourceDatabase.GroupDatabase<T> groupDatabase;
    private ResourceDatabase.ServiceProviderConfigDatabase serviceProviderConfigDatabase;

    @Override
    public void perform(GroupQueryContext<T> context) {
        int capacity = maxAllowedResults();
        List<T> queryResults = groupDatabase.query(context.getFilter(), context.getSort(), context.getAscending());
        if (queryResults.size() > capacity)
            throw ExceptionFactory.tooManyGroups(capacity, queryResults.size());

        context.setResources(queryResults);
        context.getResults().put(GroupQueryContext.TOTAL_RESULTS, queryResults.size());
    }

    private int maxAllowedResults() {
        return serviceProviderConfigDatabase.find().getFilter().getMaxResults();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(groupDatabase);
        Assert.notNull(serviceProviderConfigDatabase);
    }

    public ResourceDatabase.GroupDatabase<T> getGroupDatabase() {
        return groupDatabase;
    }

    public void setGroupDatabase(ResourceDatabase.GroupDatabase<T> groupDatabase) {
        this.groupDatabase = groupDatabase;
    }

    public ResourceDatabase.ServiceProviderConfigDatabase getServiceProviderConfigDatabase() {
        return serviceProviderConfigDatabase;
    }

    public void setServiceProviderConfigDatabase(ResourceDatabase.ServiceProviderConfigDatabase serviceProviderConfigDatabase) {
        this.serviceProviderConfigDatabase = serviceProviderConfigDatabase;
    }
}
