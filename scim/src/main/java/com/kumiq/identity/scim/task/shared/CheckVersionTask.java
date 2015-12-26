package com.kumiq.identity.scim.task.shared;

import com.kumiq.identity.scim.database.ResourceDatabase;
import com.kumiq.identity.scim.resource.core.Resource;
import com.kumiq.identity.scim.task.ResourceOpContext;
import com.kumiq.identity.scim.task.Task;
import com.kumiq.identity.scim.utils.AppUtils;
import com.kumiq.identity.scim.utils.ETagUtils;
import com.kumiq.identity.scim.exception.ExceptionFactory;
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
public abstract class CheckVersionTask<T extends Resource> implements Task<ResourceOpContext<T>> {

    public static final String IF_MATCH = "If-Match";
    public static final String VERSION_KEY = "Version";

    @Autowired
    private Environment environment;

    private ResourceDatabase<T> resourceDatabase;

    protected abstract ExceptionFactory.ResourceNotFoundException notFoundException(String id);

    protected abstract ExceptionFactory.ResourceVersionMismatchException versionMismatchException(String id, String accessETag, String actualETag);

    @Override
    public void perform(ResourceOpContext<T> context) {
        if (AppUtils.isTestProfileActive(environment))
            return;

        Assert.notNull(context.getId());

        Optional<T> result = resourceDatabase.findById(context.getId());
        if (!result.isPresent())
            throw notFoundException(context.getId());

        T resource = result.get();
        String actualETag = ETagUtils.createWeakEtag(resource.getMeta().getVersion());
        String accessETag = context.getUserInfo().containsKey(VERSION_KEY) ?
                context.getUserInfo().get(VERSION_KEY).toString() :
                context.getHttpRequest().getHeader(IF_MATCH);

        if (!actualETag.equals(accessETag))
            throw versionMismatchException(context.getId(), accessETag, actualETag);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(resourceDatabase);
    }

    public ResourceDatabase<T> getResourceDatabase() {
        return resourceDatabase;
    }

    public void setResourceDatabase(ResourceDatabase<T> resourceDatabase) {
        this.resourceDatabase = resourceDatabase;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
