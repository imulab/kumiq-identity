package com.kumiq.identity.scim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
@SpringBootApplication
@ImportResource(value = {
        "config/DataInitializationConfig.xml",
        "config/BulkExecutorsConfig.xml"
})
public class ScimApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScimApplication.class, args);
    }
}
