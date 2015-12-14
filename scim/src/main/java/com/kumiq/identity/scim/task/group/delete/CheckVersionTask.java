package com.kumiq.identity.scim.task.group.delete;

import com.kumiq.identity.scim.database.ResourceDatabase;
import com.kumiq.identity.scim.resource.group.Group;
import com.kumiq.identity.scim.resource.user.User;
import com.kumiq.identity.scim.task.GroupDeleteContext;
import com.kumiq.identity.scim.task.Task;
import com.kumiq.identity.scim.utils.AppUtils;
import com.kumiq.identity.scim.utils.ETagUtils;
import com.kumiq.identity.scim.utils.ExceptionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class CheckVersionTask<T extends Group> implements Task<GroupDeleteContext<T>> {

    public static final String IF_MATCH = "If-Match";

    @Autowired
    private Environment environment;

    private ResourceDatabase.GroupDatabase groupDatabase;

    @Override
    public void perform(GroupDeleteContext<T> context) {
        if (AppUtils.isTestProfileActive(environment))
            return;

        Optional result = groupDatabase.findById(context.getId());
        if (!result.isPresent())
            throw ExceptionFactory.groupResourceNotFound(context.getId());

        User user = (User) result.get();
        String actualETag = ETagUtils.createWeakEtag(user.getMeta().getVersion());
        String accessETag = context.getHttpRequest().getHeader(IF_MATCH);

        if (!actualETag.equals(accessETag))
            throw ExceptionFactory.groupResourceVersionMismatch(context.getId(), accessETag, actualETag);
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
