package com.kumiq.identity.scim.task

import com.kumiq.identity.scim.resource.misc.Schema
import com.kumiq.identity.scim.resource.user.User
import com.kumiq.identity.scim.task.shared.SwitchPrimaryTask
import org.junit.Assert
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
class SwitchPrimaryTaskTests {

    @Test
    void testSwitchWithNewPrimary() {
        User original = new User(emails: [
                new User.Email(value: 'foo1', primary: true),
                new User.Email(value: 'foo2', primary: false),
        ])
        User resource = new User(emails: [
                new User.Email(value: 'foo1', primary: true),
                new User.Email(value: 'foo2', primary: true),
        ])
        ReplaceContext context = new UserReplaceContext(resource: resource, originalCopy: original)
        SwitchPrimaryTask task = new SwitchPrimaryTask.SwitchEmailPrimaryTask()

        task.perform(context)

        Assert.assertFalse(resource.emails[0].primary)
        Assert.assertTrue(resource.emails[1].primary)
    }

    @Test
    void testSwitchWithNoNewPrimary() {
        User original = new User(emails: [
                new User.Email(value: 'foo1', primary: true),
                new User.Email(value: 'foo2', primary: false),
        ])
        User resource = new User(emails: [
                new User.Email(value: 'foo1', primary: false),
                new User.Email(value: 'foo2', primary: false),
        ])
        ReplaceContext context = new UserReplaceContext(resource: resource, originalCopy: original)
        SwitchPrimaryTask task = new SwitchPrimaryTask.SwitchEmailPrimaryTask()

        task.perform(context)

        Assert.assertFalse(resource.emails[0].primary)
        Assert.assertFalse(resource.emails[1].primary)
    }

    @Test
    void testSwitchWithUnchanged() {
        User original = new User(emails: [
                new User.Email(value: 'foo1', primary: true),
                new User.Email(value: 'foo2', primary: false),
        ])
        User resource = new User(emails: [
                new User.Email(value: 'foo1', primary: true),
                new User.Email(value: 'foo2', primary: false),
        ])
        ReplaceContext context = new UserReplaceContext(resource: resource, originalCopy: original)
        SwitchPrimaryTask task = new SwitchPrimaryTask.SwitchEmailPrimaryTask()

        task.perform(context)

        Assert.assertTrue(resource.emails[0].primary)
        Assert.assertFalse(resource.emails[1].primary)
    }

    @Test
    void testSwitchWithNoOldPrimary() {
        User original = new User(emails: [
                new User.Email(value: 'foo1', primary: false),
                new User.Email(value: 'foo2', primary: false),
        ])
        User resource = new User(emails: [
                new User.Email(value: 'foo1', primary: true),
                new User.Email(value: 'foo2', primary: false),
        ])
        ReplaceContext context = new UserReplaceContext(resource: resource, originalCopy: original)
        SwitchPrimaryTask task = new SwitchPrimaryTask.SwitchEmailPrimaryTask()

        task.perform(context)

        Assert.assertTrue(resource.emails[0].primary)
        Assert.assertFalse(resource.emails[1].primary)
    }
}
