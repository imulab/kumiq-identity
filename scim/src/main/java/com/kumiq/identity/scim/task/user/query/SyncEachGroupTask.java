package com.kumiq.identity.scim.task.user.query;

import com.kumiq.identity.scim.resource.user.User;
import com.kumiq.identity.scim.task.Task;
import com.kumiq.identity.scim.task.UserGetContext;
import com.kumiq.identity.scim.task.UserQueryContext;
import com.kumiq.identity.scim.task.shared.SyncGroupTask;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class SyncEachGroupTask<T extends User> implements Task<UserQueryContext<T>> {

    private SyncGroupTask<T> syncGroupTask;

    @Override
    public void perform(UserQueryContext<T> context) {
        if (CollectionUtils.isEmpty(context.getResources()))
            return;

        List<T> processed = context.getResources().stream()
                .map(t -> {
                    UserGetContext<T> dummyContext = new UserGetContext<>();
                    dummyContext.setResource(t);
                    syncGroupTask.perform(dummyContext);
                    return dummyContext.getResource();
                })
                .collect(Collectors.toList());
        context.setResources(processed);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(syncGroupTask);
    }

    public SyncGroupTask<T> getSyncGroupTask() {
        return syncGroupTask;
    }

    public void setSyncGroupTask(SyncGroupTask<T> syncGroupTask) {
        this.syncGroupTask = syncGroupTask;
    }
}
