package com.kumiq.identity.scim.task.user.create;

import com.kumiq.identity.scim.resource.constant.ScimConstants;
import com.kumiq.identity.scim.resource.core.Meta;
import com.kumiq.identity.scim.resource.user.User;
import com.kumiq.identity.scim.task.Task;
import com.kumiq.identity.scim.task.UserCreateContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class CreateMetaTask<T extends User> implements Task<UserCreateContext<T>> {

    @Value("${server.baseUrl:http://localhost:8080}")
    private String serverBaseUrl;

    @Override
    public void perform(UserCreateContext<T> context) {
        Assert.notNull(context.getResource().getId());

        Meta meta = new Meta();
        meta.setResourceType(ScimConstants.RESOURCE_TYPE_USER);
        Date creationTime = new Date();
        meta.setCreated(creationTime);
        meta.setLastModified(creationTime);
        meta.setLocation(serverBaseUrl + "/Users/" + context.getResource().getId());
        meta.setVersion(1l);

        context.getResource().setMeta(meta);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public String getServerBaseUrl() {
        return serverBaseUrl;
    }

    public void setServerBaseUrl(String serverBaseUrl) {
        this.serverBaseUrl = serverBaseUrl;
    }
}
