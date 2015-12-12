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
        "config/GroupGetTaskConfig.xml"
})
public class TaskConfiguration {
}
