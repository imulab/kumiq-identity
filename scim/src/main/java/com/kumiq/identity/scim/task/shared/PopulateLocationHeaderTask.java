package com.kumiq.identity.scim.task.shared;

import com.kumiq.identity.scim.resource.core.Resource;
import com.kumiq.identity.scim.task.ResourceOpContext;
import com.kumiq.identity.scim.task.Task;
import org.springframework.util.StringUtils;

/**
 * Populate resource's location header into http response
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class PopulateLocationHeaderTask<T extends Resource> implements Task<ResourceOpContext<T>> {

    public static final String LOCATION = "Location";

    @Override
    public void perform(ResourceOpContext<T> context) {
        String location = context.getResource().getMeta().getLocation();
        if (!StringUtils.isEmpty(location))
            context.getHttpResponse().setHeader(LOCATION, location);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
