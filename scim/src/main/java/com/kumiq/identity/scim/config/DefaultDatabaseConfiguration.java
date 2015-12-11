package com.kumiq.identity.scim.config;

import com.kumiq.identity.scim.database.InMemoryDatabase;
import com.kumiq.identity.scim.database.ResourceDatabase;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
@Configuration
public class DefaultDatabaseConfiguration {

    @Bean(name = "userDatabase")
    @ConditionalOnMissingBean(ResourceDatabase.UserDatabase.class)
    public ResourceDatabase.UserDatabase userDatabase() {
        return new InMemoryDatabase.UserInMemoryDatabase<>();
    }

    @Bean(name = "groupDatabase")
    @ConditionalOnMissingBean(ResourceDatabase.GroupDatabase.class)
    public ResourceDatabase.GroupDatabase groupDatabase() {
        return new InMemoryDatabase.GroupInMemoryDatabase<>();
    }

    @Bean(name = "schemaDatabase")
    @ConditionalOnMissingBean(ResourceDatabase.SchemaDatabase.class)
    public ResourceDatabase.SchemaDatabase schemaDatabase() {
        return new InMemoryDatabase.SchemaInMemoryDatabase();
    }

    @Bean(name = "resourceTypeDatabase")
    @ConditionalOnMissingBean(ResourceDatabase.ResourceTypeDatabase.class)
    public ResourceDatabase.ResourceTypeDatabase resourceTypeDatabase() {
        return new InMemoryDatabase.ResourceTypeInMemoryDatabase();
    }

    @Bean(name = "serviceProviderConfigDatabase")
    @ConditionalOnMissingBean(ResourceDatabase.ServiceProviderConfigDatabase.class)
    public ResourceDatabase.ServiceProviderConfigDatabase serviceProviderConfigDatabase() {
        return new InMemoryDatabase.ServiceProviderConfigInMemoryDatabase();
    }
}
