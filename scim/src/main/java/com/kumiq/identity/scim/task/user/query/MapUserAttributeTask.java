package com.kumiq.identity.scim.task.user.query;

import com.kumiq.identity.scim.path.Configuration;
import com.kumiq.identity.scim.path.MappingContext;
import com.kumiq.identity.scim.path.ResourceMapper;
import com.kumiq.identity.scim.resource.user.User;
import com.kumiq.identity.scim.task.Task;
import com.kumiq.identity.scim.task.UserQueryContext;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class MapUserAttributeTask<T extends User> implements Task<UserQueryContext<T>> {

    @Override
    public void perform(UserQueryContext<T> context) {
        Assert.notNull(context.getSchema());
        List<Map<String, Object>> mappedResources = new ArrayList<>();

        context.getResources().forEach(t -> {
            MappingContext mappingContext = new MappingContext(t, context.getSchema(), context.getAttributes(), new ArrayList<>());
            Configuration mappingConfiguration = Configuration
                    .withResourceObjectProvider()
                    .withOption(Configuration.Option.API_ATTR_NAME_PREF)
                    .withOption(Configuration.Option.COMPILE_WITH_HINT);
            Map<String, Object> mapped = ResourceMapper.convertToMap(mappingContext, mappingConfiguration);
            mappedResources.add(mapped);
        });

        context.getResults().put(UserQueryContext.RESOURCES, mappedResources);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
