package com.kumiq.identity.scim.task

import com.kumiq.identity.scim.exception.ExceptionFactory
import com.kumiq.identity.scim.resource.user.User
import com.kumiq.identity.scim.task.shared.CheckUniquePrimaryTask
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 *
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
@RunWith(JUnit4)
class CheckUniquePrimaryTaskTests {

    CheckUniquePrimaryTask task

    @Test(expected = ExceptionFactory.ResourceMultiplePrimaryException)
    void testMultiplePrimary() {
        User user = new User(emails: [
                new User.Email(value: 'a', primary: true),
                new User.Email(value: 'b', primary: true),
                new User.Email(value: 'c', primary: false),
        ])
        UserCreateContext context = new UserCreateContext(resource: user);

        CheckUniquePrimaryTask task = new CheckUniquePrimaryTask.CheckEmailUniquePrimaryTask()
        task.perform(context);
    }

    @Test
    void testSinglePrimary() {
        User user = new User(emails: [
                new User.Email(value: 'a', primary: true),
                new User.Email(value: 'b', primary: false),
                new User.Email(value: 'c', primary: false),
        ])
        UserCreateContext context = new UserCreateContext(resource: user);

        CheckUniquePrimaryTask task = new CheckUniquePrimaryTask.CheckEmailUniquePrimaryTask()
        task.perform(context);
    }
}
