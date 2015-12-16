package com.kumiq.identity.scim.task.shared;

import com.kumiq.identity.scim.database.ResourceDatabase;
import com.kumiq.identity.scim.resource.group.Group;
import com.kumiq.identity.scim.resource.user.User;
import com.kumiq.identity.scim.task.ResourceOpContext;
import com.kumiq.identity.scim.task.Task;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class UpdateUserGroupTask<T extends Group> implements Task<ResourceOpContext<T>> {

    private ResourceDatabase.UserDatabase userDatabase;

    @Override
    @SuppressWarnings("unchecked")
    public void perform(ResourceOpContext<T> context) {
        context.getResource().getMembers().forEach(u -> {
            Optional result = userDatabase.findById(u.getValue());
            if (result.isPresent()) {
                User user = (User) result.get();
                if (!user.hasGroup(context.getResource().getId())) {
                    User.Group group = new User.Group();
                    group.setValue(context.getResource().getId());
                    group.set$ref(context.getResource().getMeta().getLocation());
                    group.setDisplay(context.getResource().getDisplayName());
                    group.setType(context.getResource().getMeta().getResourceType());
                    user.getGroups().add(group);
                    userDatabase.save(user);
                }
            }
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(userDatabase);
    }

    public ResourceDatabase.UserDatabase getUserDatabase() {
        return userDatabase;
    }

    public void setUserDatabase(ResourceDatabase.UserDatabase userDatabase) {
        this.userDatabase = userDatabase;
    }
}
