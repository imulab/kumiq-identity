package com.kumiq.identity.scim.task.user.get;

import com.kumiq.identity.scim.path.Configuration;
import com.kumiq.identity.scim.path.MappingContext;
import com.kumiq.identity.scim.path.ResourceMapper;
import com.kumiq.identity.scim.resource.user.User;
import com.kumiq.identity.scim.task.Task;
import com.kumiq.identity.scim.task.UserGetContext;
import org.springframework.util.Assert;

import java.util.ArrayList;

/**
 * Convert a resource to map for returning purpose. Resource's returned attribute will be taken into account.
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class ConvertUserToMapTask<T extends User> implements Task<UserGetContext<T>> {

    @Override
    public void perform(UserGetContext<T> context) {
        Assert.notNull(context.getResource());
        Assert.notNull(context.getSchema());

        MappingContext mappingContext = new MappingContext(
                context.getResource(),
                context.getSchema(),
                new ArrayList<>(),
                new ArrayList<>());
        Configuration configuration = Configuration
                .withResourceObjectProvider()
                .withOption(Configuration.Option.COMPILE_WITH_HINT);

        context.setData(ResourceMapper.convertToMap(mappingContext, configuration));
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
