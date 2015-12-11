package com.kumiq.identity.scim.task.shared;

import com.kumiq.identity.scim.resource.core.Resource;
import com.kumiq.identity.scim.task.ResourceOpContext;
import com.kumiq.identity.scim.task.Task;
import com.kumiq.identity.scim.utils.ETagUtils;

/**
 * Populate weak etag into http response
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class PopulateETagHeaderTask<T extends Resource> implements Task<ResourceOpContext<T>> {

    public static final String ETAG = "ETag";

    @Override
    public void perform(ResourceOpContext<T> context) {
        String weakETag = ETagUtils.createWeakEtag(context.getResource().getMeta().getVersion());
        context.getHttpResponse().setHeader(ETAG, weakETag);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
