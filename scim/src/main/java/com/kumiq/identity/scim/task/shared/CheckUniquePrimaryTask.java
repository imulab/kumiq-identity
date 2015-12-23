package com.kumiq.identity.scim.task.shared;

import com.kumiq.identity.scim.exception.ExceptionFactory;
import com.kumiq.identity.scim.resource.core.Resource;
import com.kumiq.identity.scim.resource.user.User;
import com.kumiq.identity.scim.task.ResourceOpContext;
import com.kumiq.identity.scim.task.Task;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public abstract class CheckUniquePrimaryTask<T extends Resource, M> implements Task<ResourceOpContext<T>> {

    protected abstract String subjectPath();

    protected abstract Collection<M> getSubjects(T resource);

    protected abstract Predicate<M> primaryPredicate();

    @Override
    public void perform(ResourceOpContext<T> context) {
        Assert.notNull(context.getResource());

        Collection<M> subjects = getSubjects(context.getResource());
        if (CollectionUtils.isEmpty(subjects))
            return;

        Long primaryCount = subjects.parallelStream()
                .filter(m -> primaryPredicate().test(m))
                .count();
        if (primaryCount > 1l) {
            throw new ExceptionFactory.ResourceMultiplePrimaryException(
                    context.getResource().getMeta().getResourceType(),
                    context.getResource().getId(),
                    subjectPath());
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    public static class CheckEmailUniquePrimaryTask<T extends User> extends CheckUniquePrimaryTask<T, User.Email> {
        @Override
        protected String subjectPath() {
            return "emails";
        }

        @Override
        protected Collection<User.Email> getSubjects(T resource) {
            return resource.getEmails();
        }

        @Override
        protected Predicate<User.Email> primaryPredicate() {
            return User.Email::isPrimary;
        }
    }

    public static class CheckPhoneNumberUniquePrimaryTask<T extends User> extends CheckUniquePrimaryTask<T, User.PhoneNumber> {
        @Override
        protected String subjectPath() {
            return "phoneNumbers";
        }

        @Override
        protected Collection<User.PhoneNumber> getSubjects(T resource) {
            return resource.getPhoneNumbers();
        }

        @Override
        protected Predicate<User.PhoneNumber> primaryPredicate() {
            return User.PhoneNumber::isPrimary;
        }
    }

    public static class CheckIMSUniquePrimaryTask<T extends User> extends CheckUniquePrimaryTask<T, User.IMS> {
        @Override
        protected String subjectPath() {
            return "ims";
        }

        @Override
        protected Collection<User.IMS> getSubjects(T resource) {
            return resource.getIms();
        }

        @Override
        protected Predicate<User.IMS> primaryPredicate() {
            return User.IMS::isPrimary;
        }
    }

    public static class CheckPhotoUniquePrimaryTask<T extends User> extends CheckUniquePrimaryTask<T, User.Photo> {
        @Override
        protected String subjectPath() {
            return "photos";
        }

        @Override
        protected Collection<User.Photo> getSubjects(T resource) {
            return resource.getPhotos();
        }

        @Override
        protected Predicate<User.Photo> primaryPredicate() {
            return User.Photo::isPrimary;
        }
    }

    public static class CheckAddressUniquePrimaryTask<T extends User> extends CheckUniquePrimaryTask<T, User.Address> {
        @Override
        protected String subjectPath() {
            return "addresses";
        }

        @Override
        protected Collection<User.Address> getSubjects(T resource) {
            return resource.getAddresses();
        }

        @Override
        protected Predicate<User.Address> primaryPredicate() {
            return User.Address::isPrimary;
        }
    }

    public static class CheckEntitlementUniquePrimaryTask<T extends User> extends CheckUniquePrimaryTask<T, User.Entitlement> {
        @Override
        protected String subjectPath() {
            return "entitlements";
        }

        @Override
        protected Collection<User.Entitlement> getSubjects(T resource) {
            return resource.getEntitlements();
        }

        @Override
        protected Predicate<User.Entitlement> primaryPredicate() {
            return User.Entitlement::isPrimary;
        }
    }

    public static class CheckRoleUniquePrimaryTask<T extends User> extends CheckUniquePrimaryTask<T, User.Role> {
        @Override
        protected String subjectPath() {
            return "roles";
        }

        @Override
        protected Collection<User.Role> getSubjects(T resource) {
            return resource.getRoles();
        }

        @Override
        protected Predicate<User.Role> primaryPredicate() {
            return User.Role::isPrimary;
        }
    }

    public static class CheckX509CertificateUniquePrimaryTask<T extends User> extends CheckUniquePrimaryTask<T, User.X509Certificate> {
        @Override
        protected String subjectPath() {
            return "x509Certificates";
        }

        @Override
        protected Collection<User.X509Certificate> getSubjects(T resource) {
            return resource.getX509Certificates();
        }

        @Override
        protected Predicate<User.X509Certificate> primaryPredicate() {
            return User.X509Certificate::isPrimary;
        }
    }
}
