package com.kumiq.identity.scim.task.user.create;

import com.kumiq.identity.scim.database.ResourceDatabase;
import com.kumiq.identity.scim.resource.constant.ScimConstants;
import com.kumiq.identity.scim.resource.group.Group;
import com.kumiq.identity.scim.resource.user.User;
import com.kumiq.identity.scim.task.Task;
import com.kumiq.identity.scim.task.UserCreateContext;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class UpdateGroupMembershipTask<T extends User> implements Task<UserCreateContext<T>> {

    private ResourceDatabase.GroupDatabase groupDatabase;

    @Override
    @SuppressWarnings("unchecked")
    public void perform(UserCreateContext<T> context) {
        context.getResource().getGroups().forEach(g -> {
            Optional result = groupDatabase.findById(g.getValue());
            if (result.isPresent()) {
                Group group = (Group) result.get();
                if (!group.hasMember(context.getResource().getId())) {
                    Group.Member member = new Group.Member();
                    member.setValue(context.getResource().getId());
                    member.set$ref(context.getResource().getMeta().getLocation());
                    member.setDisplay(context.getResource().getDisplayName());
                    member.setType(context.getResource().getMeta().getResourceType());
                    group.getMembers().add(member);
                    groupDatabase.save(group);
                }
            }
        });
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
