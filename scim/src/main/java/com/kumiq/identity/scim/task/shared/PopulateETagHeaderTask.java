package com.kumiq.identity.scim.task.shared;

import com.kumiq.identity.scim.resource.core.Resource;
import com.kumiq.identity.scim.task.ResourceOpContext;
import com.kumiq.identity.scim.task.Task;
import com.kumiq.identity.scim.utils.AppUtils;
import com.kumiq.identity.scim.utils.ETagUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

/**
 * Populate weak etag into http response
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class PopulateETagHeaderTask<T extends Resource> implements Task<ResourceOpContext<T>> {

    public static final String ETAG = "ETag";

    @Autowired
    private Environment environment;

    @Override
    public void perform(ResourceOpContext<T> context) {
        if (AppUtils.isTestProfileActive(environment))
            return;

        String weakETag = ETagUtils.createWeakEtag(context.getResource().getMeta().getVersion());
        context.getHttpResponse().setHeader(ETAG, weakETag);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
