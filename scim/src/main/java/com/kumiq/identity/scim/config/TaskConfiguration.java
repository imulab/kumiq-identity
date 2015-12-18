package com.kumiq.identity.scim.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
@Configuration
@ImportResource({
        "config/UserGetTaskConfig.xml",
        "config/GroupGetTaskConfig.xml",
        "config/UserDeleteTaskConfig.xml",
        "config/GroupDeleteTaskConfig.xml",
        "config/UserQueryTaskConfig.xml",
        "config/GroupQueryTaskConfig.xml",
        "config/UserCreateTaskConfig.xml",
        "config/GroupCreateTaskConfig.xml",
        "config/UserReplaceTaskConfig.xml",
        "config/GroupReplaceTaskConfig.xml"
})
public class TaskConfiguration {
}
