package com.kumiq.identity.scim.task.user.delete;

import com.kumiq.identity.scim.database.ResourceDatabase;
import com.kumiq.identity.scim.resource.user.User;
import com.kumiq.identity.scim.task.Task;
import com.kumiq.identity.scim.task.UserDeleteContext;
import com.kumiq.identity.scim.task.shared.PopulateETagHeaderTask;
import com.kumiq.identity.scim.utils.AppUtils;
import com.kumiq.identity.scim.utils.ETagUtils;
import com.kumiq.identity.scim.utils.ExceptionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * Check if the resource which was requested to delete matches the latest version.
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class CheckVersionTask<T extends User> implements Task<UserDeleteContext<T>> {

    public static final String IF_MATCH = "If-Match";

    @Autowired
    private Environment environment;

    private ResourceDatabase.UserDatabase userDatabase;

    @Override
    public void perform(UserDeleteContext<T> context) {
        if (AppUtils.isTestProfileActive(environment))
            return;

        Optional result = userDatabase.findById(context.getId());
        if (!result.isPresent())
            throw ExceptionFactory.userResourceNotFound(context.getId());

        User user = (User) result.get();
        String actualETag = ETagUtils.createWeakEtag(user.getMeta().getVersion());
        String accessETag = context.getHttpRequest().getHeader(IF_MATCH);

        if (!actualETag.equals(accessETag))
            throw ExceptionFactory.userResourceVersionMismatch(context.getId(), accessETag, actualETag);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(userDatabase);
    }

    public ResourceDatabase.UserDatabase getUserDatabase() {
        return userDatabase;
    }

    public void setUserDatabase(ResourceDatabase.UserDatabase userDatabase) {
        this.userDatabase = userDatabase;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
