package com.kumiq.identity.scim.task.user.query;

import com.kumiq.identity.scim.database.ResourceDatabase;
import com.kumiq.identity.scim.resource.user.User;
import com.kumiq.identity.scim.task.Task;
import com.kumiq.identity.scim.task.UserQueryContext;
import com.kumiq.identity.scim.exception.ExceptionFactory;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Query the database and set results for further processing
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class QueryUserTask<T extends User> implements Task<UserQueryContext<T>> {

    private ResourceDatabase.UserDatabase<T> userDatabase;
    private ResourceDatabase.ServiceProviderConfigDatabase serviceProviderConfigDatabase;

    @Override
    public void perform(UserQueryContext<T> context) {
        int capacity = maxAllowedResults();
        List<T> queryResults = userDatabase.query(context.getFilter(), context.getSort(), context.getAscending());
        if (queryResults.size() > capacity)
            throw ExceptionFactory.tooManyUsers(capacity, queryResults.size());

        context.setResources(queryResults);
        context.getResults().put(UserQueryContext.TOTAL_RESULTS, queryResults.size());
    }

    private int maxAllowedResults() {
        return serviceProviderConfigDatabase.find().getFilter().getMaxResults();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(userDatabase);
        Assert.notNull(serviceProviderConfigDatabase);
    }

    public ResourceDatabase.UserDatabase<T> getUserDatabase() {
        return userDatabase;
    }

    public void setUserDatabase(ResourceDatabase.UserDatabase<T> userDatabase) {
        this.userDatabase = userDatabase;
    }

    public ResourceDatabase.ServiceProviderConfigDatabase getServiceProviderConfigDatabase() {
        return serviceProviderConfigDatabase;
    }

    public void setServiceProviderConfigDatabase(ResourceDatabase.ServiceProviderConfigDatabase serviceProviderConfigDatabase) {
        this.serviceProviderConfigDatabase = serviceProviderConfigDatabase;
    }
}
