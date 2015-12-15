package com.kumiq.identity.scim.task.group.query;

import com.kumiq.identity.scim.path.Configuration;
import com.kumiq.identity.scim.path.MappingContext;
import com.kumiq.identity.scim.path.ResourceMapper;
import com.kumiq.identity.scim.resource.group.Group;
import com.kumiq.identity.scim.task.GroupQueryContext;
import com.kumiq.identity.scim.task.Task;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TODO consider merge with {@link com.kumiq.identity.scim.task.user.query.MapUserAttributeTask}
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class MapGroupAttributeTask<T extends Group> implements Task<GroupQueryContext<T>> {

    @Override
    public void perform(GroupQueryContext<T> context) {
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

        context.getResults().put(GroupQueryContext.RESOURCES, mappedResources);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
