package com.kumiq.identity.scim.task;

import org.springframework.beans.factory.InitializingBean;

/**
 * Perform an action on {@link ResourceOpContext}
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
public interface Task<T extends ResourceOpContext> extends InitializingBean {

    /**
     * Perform an action
     *
     * @param context
     */
    void perform(T context);

    /**
     * Task name
     *
     * @return
     */
    default String taskName() {
        return this.getClass().getSimpleName();
    }
}
