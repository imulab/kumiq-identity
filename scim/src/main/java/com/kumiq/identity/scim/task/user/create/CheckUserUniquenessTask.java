package com.kumiq.identity.scim.task.user.create;

import com.kumiq.identity.scim.database.ResourceDatabase;
import com.kumiq.identity.scim.resource.user.User;
import com.kumiq.identity.scim.task.shared.CheckUniquenessTask;
import com.kumiq.identity.scim.exception.ExceptionFactory;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class CheckUserUniquenessTask<T extends User> extends CheckUniquenessTask<T> {

    private ResourceDatabase.UserDatabase<T> userDatabase;

    @Override
    protected List<T> performQuery(String query, String sort, boolean ascending) {
        return userDatabase.query(query, sort, ascending);
    }

    @Override
    protected ExceptionFactory.ResourceUniquenessViolatedException violationException(String path, String resourceId) {
        return ExceptionFactory.userPathNotUnique(path, resourceId);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(userDatabase);
    }

    public ResourceDatabase.UserDatabase<T> getUserDatabase() {
        return userDatabase;
    }

    public void setUserDatabase(ResourceDatabase.UserDatabase<T> userDatabase) {
        this.userDatabase = userDatabase;
    }
}
