package com.kumiq.identity.scim.task.shared;

import com.kumiq.identity.scim.resource.core.Resource;
import com.kumiq.identity.scim.task.ResourceOpContext;
import com.kumiq.identity.scim.task.Task;
import com.kumiq.identity.scim.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/**
 * Populate resource's location header into http response
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class PopulateLocationHeaderTask<T extends Resource> implements Task<ResourceOpContext<T>> {

    public static final String LOCATION = "Location";

    @Autowired
    Environment environment;

    @Override
    public void perform(ResourceOpContext<T> context) {
        if (AppUtils.isTestProfileActive(environment))
            return;

        String location = context.getResource().getMeta().getLocation();
        if (!StringUtils.isEmpty(location))
            context.getHttpResponse().setHeader(LOCATION, location);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public static String getLOCATION() {
        return LOCATION;
    }
}
