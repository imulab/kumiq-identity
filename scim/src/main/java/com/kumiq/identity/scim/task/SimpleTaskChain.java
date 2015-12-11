package com.kumiq.identity.scim.task;

import com.kumiq.identity.scim.resource.core.Resource;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class SimpleTaskChain<T extends Resource> implements Task<ResourceOpContext<T>> {

    private List<Task<ResourceOpContext<T>>> tasks = new ArrayList<>();
    private String taskName;

    @Override
    public void perform(ResourceOpContext<T> context) {
        for (Task<ResourceOpContext<T>> task : tasks)
            task.perform(context);
    }

    @Override
    public String taskName() {
        return this.taskName;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(tasks);
        Assert.notNull(taskName);
    }

    public List<Task<ResourceOpContext<T>>> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task<ResourceOpContext<T>>> tasks) {
        this.tasks = tasks;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
