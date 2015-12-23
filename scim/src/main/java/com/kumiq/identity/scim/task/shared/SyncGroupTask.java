package com.kumiq.identity.scim.task.shared;

import com.kumiq.identity.scim.database.ResourceDatabase;
import com.kumiq.identity.scim.resource.constant.ScimConstants;
import com.kumiq.identity.scim.resource.group.Group;
import com.kumiq.identity.scim.resource.user.User;
import com.kumiq.identity.scim.task.ResourceOpContext;
import com.kumiq.identity.scim.task.Task;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class SyncGroupTask<T extends User> implements Task<ResourceOpContext<T>> {

    private ResourceDatabase.GroupDatabase<? extends Group> groupDatabase;

    @Override
    public void perform(ResourceOpContext<T> context) {
        Assert.notNull(context.getResource());

        List<? extends Group> groups = groupDatabase.findGroupsWithMember(context.getResource().getId(), true);
        if (CollectionUtils.isEmpty(groups))
            return;

        List<User.Group> directGroups = context.getResource().getGroups();
        List<User.Group> allGroups = groups
                .parallelStream()
                .map(group -> {
                    User.Group g = new User.Group();
                    g.setValue(group.getId());
                    g.set$ref(group.getMeta().getLocation());
                    g.setDisplay(group.getDisplayName());
                    if (directGroups.stream().filter(e -> e.getValue().equals(g.getValue())).findAny().isPresent()) {
                        g.setType(ScimConstants.GROUP_TYPE_DIRECT);
                    } else {
                        g.setType(ScimConstants.GROUP_TYPE_INDIRECT);
                    }
                    return g;
                })
                .collect(Collectors.toList());
        context.getResource().setGroups(allGroups);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(groupDatabase);
    }

    public ResourceDatabase.GroupDatabase<? extends Group> getGroupDatabase() {
        return groupDatabase;
    }

    public void setGroupDatabase(ResourceDatabase.GroupDatabase<? extends Group> groupDatabase) {
        this.groupDatabase = groupDatabase;
    }
}
