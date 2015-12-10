package com.kumiq.identity.scim.init

import com.kumiq.identity.scim.database.InMemoryDatabase
import com.kumiq.identity.scim.database.ResourceDatabase
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

/**
 *
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner)
@SpringApplicationConfiguration(classes = [TestConfiguration])
class GenericResourceInitializationTests {

    @Autowired
    ResourceDatabase.UserDatabase userDatabase

    @Test
    void testBootstrapSuccess() {
        Assert.assertEquals(3, userDatabase.findAll().size())
    }

    @Configuration
    static class TestConfiguration {

        @Bean
        ResourceDatabase.UserDatabase userDatabase() {
            new InMemoryDatabase.UserInMemoryDatabase();
        }

        @Bean(name = 'userInit')
        GenericResourceInitialization userInit() {
            new GenericResourceInitialization(
                    filePath: 'GenericResourceInitializationTests/testBootstrapSuccess.json',
                    database: userDatabase(),
                    wrapperClass: ResourceInitWrapper.UserResourceInitWrapper
            )
        }
    }
}
