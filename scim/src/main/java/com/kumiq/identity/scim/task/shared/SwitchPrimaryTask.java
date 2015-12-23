package com.kumiq.identity.scim.task.shared;

import com.kumiq.identity.scim.resource.core.Resource;
import com.kumiq.identity.scim.resource.user.User;
import com.kumiq.identity.scim.task.ReplaceContext;
import com.kumiq.identity.scim.task.Task;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.kumiq.identity.scim.utils.ValueUtils.nullSafeEquals;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public abstract class SwitchPrimaryTask<T extends Resource, M> implements Task<ReplaceContext<T>> {

    protected abstract Collection<M> getSubjects(T resource);

    protected abstract Predicate<M> primaryPredicate();

    protected abstract Consumer<M> disablePrimary();

    public BiPredicate<M, M> equalsPredicate() {
        return M::equals;
    }

    protected Collection<M> getPrimarySubjects(T resource) {
        Collection<M> subjects = getSubjects(resource);
        if (CollectionUtils.isEmpty(subjects))
            return new ArrayList<>();

        return subjects.parallelStream()
                .filter(m -> primaryPredicate().test(m))
                .collect(Collectors.toList());
    }

    @Override
    public void perform(ReplaceContext<T> context) {
        Assert.notNull(context.getOriginalCopy());
        Assert.notNull(context.getResource());

        Collection<M> originalPrimaries = getPrimarySubjects(context.getOriginalCopy());
        if (CollectionUtils.isEmpty(originalPrimaries))
            return;
        Assert.isTrue(originalPrimaries.size() == 1);
        M originalPrimary = originalPrimaries.iterator().next();

        Collection<M> newPrimaries = getPrimarySubjects(context.getResource());
        if (CollectionUtils.isEmpty(newPrimaries))
            return;
        if (newPrimaries.size() > 1)
            newPrimaries.parallelStream()
                    .filter(m -> equalsPredicate().test(m, originalPrimary))
                    .forEach(m -> disablePrimary().accept(m));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    public static class SwitchEmailPrimaryTask<T extends User> extends SwitchPrimaryTask<T, User.Email> {
        @Override
        protected Collection<User.Email> getSubjects(T resource) {
            return resource.getEmails();
        }

        @Override
        protected Predicate<User.Email> primaryPredicate() {
            return User.Email::isPrimary;
        }

        @Override
        protected Consumer<User.Email> disablePrimary() {
            return email -> email.setPrimary(Boolean.FALSE);
        }

        @Override
        public BiPredicate<User.Email, User.Email> equalsPredicate() {
            return (email1, email2) -> nullSafeEquals(email1.getValue(), email2.getValue());
        }
    }

    public static class SwitchPhoneNumberPrimaryTask<T extends User> extends SwitchPrimaryTask<T, User.PhoneNumber> {
        @Override
        protected Collection<User.PhoneNumber> getSubjects(T resource) {
            return resource.getPhoneNumbers();
        }

        @Override
        protected Predicate<User.PhoneNumber> primaryPredicate() {
            return User.PhoneNumber::isPrimary;
        }

        @Override
        protected Consumer<User.PhoneNumber> disablePrimary() {
            return phoneNumber -> phoneNumber.setPrimary(Boolean.FALSE);
        }

        @Override
        public BiPredicate<User.PhoneNumber, User.PhoneNumber> equalsPredicate() {
            return (phoneNumber1, phoneNumber2) -> nullSafeEquals(phoneNumber1.getValue(), phoneNumber2.getValue());
        }
    }

    public static class SwitchIMSPrimaryTask<T extends User> extends SwitchPrimaryTask<T, User.IMS> {
        @Override
        protected Collection<User.IMS> getSubjects(T resource) {
            return resource.getIms();
        }

        @Override
        protected Predicate<User.IMS> primaryPredicate() {
            return User.IMS::isPrimary;
        }

        @Override
        protected Consumer<User.IMS> disablePrimary() {
            return ims -> ims.setPrimary(Boolean.FALSE);
        }

        @Override
        public BiPredicate<User.IMS, User.IMS> equalsPredicate() {
            return (ims1, ims2) -> (nullSafeEquals(ims1.getValue(), ims2.getValue()) &&
                    nullSafeEquals(ims1.getType(), ims2.getType()));
        }
    }

    public static class SwitchPhotoPrimaryTask<T extends User> extends SwitchPrimaryTask<T, User.Photo> {
        @Override
        protected Collection<User.Photo> getSubjects(T resource) {
            return resource.getPhotos();
        }

        @Override
        protected Predicate<User.Photo> primaryPredicate() {
            return User.Photo::isPrimary;
        }

        @Override
        protected Consumer<User.Photo> disablePrimary() {
            return photo -> photo.setPrimary(Boolean.FALSE);
        }

        @Override
        public BiPredicate<User.Photo, User.Photo> equalsPredicate() {
            return (photo1, photo2) -> (nullSafeEquals(photo1.getValue(), photo2.getValue()) &&
                    nullSafeEquals(photo1.getType(), photo2.getType()));
        }
    }

    public static class SwitchAddressPrimaryTask<T extends User> extends SwitchPrimaryTask<T, User.Address> {
        @Override
        protected Collection<User.Address> getSubjects(T resource) {
            return resource.getAddresses();
        }

        @Override
        protected Predicate<User.Address> primaryPredicate() {
            return User.Address::isPrimary;
        }

        @Override
        protected Consumer<User.Address> disablePrimary() {
            return address -> address.setPrimary(Boolean.FALSE);
        }

        @Override
        public BiPredicate<User.Address, User.Address> equalsPredicate() {
            return (address1, address2) -> (
                    nullSafeEquals(address1.getStreetAddress(), address2.getStreetAddress()) &&
                    nullSafeEquals(address1.getLocality(), address2.getLocality()) &&
                    nullSafeEquals(address1.getRegion(), address2.getRegion()) &&
                    nullSafeEquals(address1.getCountry(), address2.getCountry()) &&
                    nullSafeEquals(address1.getPostalCode(), address2.getPostalCode()) &&
                    nullSafeEquals(address1.getType(), address2.getType()));
        }
    }

    public static class SwitchEntitlementPrimaryTask<T extends User> extends SwitchPrimaryTask<T, User.Entitlement> {
        @Override
        protected Collection<User.Entitlement> getSubjects(T resource) {
            return resource.getEntitlements();
        }

        @Override
        protected Predicate<User.Entitlement> primaryPredicate() {
            return User.Entitlement::isPrimary;
        }

        @Override
        protected Consumer<User.Entitlement> disablePrimary() {
            return entitlement -> entitlement.setPrimary(Boolean.FALSE);
        }

        @Override
        public BiPredicate<User.Entitlement, User.Entitlement> equalsPredicate() {
            return (entitlement1, entitlement2) -> (nullSafeEquals(entitlement1.getValue(), entitlement2.getValue()) &&
                            nullSafeEquals(entitlement1.getType(), entitlement2.getType()));
        }
    }

    public static class SwitchRolePrimaryTask<T extends User> extends SwitchPrimaryTask<T, User.Role> {
        @Override
        protected Collection<User.Role> getSubjects(T resource) {
            return resource.getRoles();
        }

        @Override
        protected Predicate<User.Role> primaryPredicate() {
            return User.Role::isPrimary;
        }

        @Override
        protected Consumer<User.Role> disablePrimary() {
            return role -> role.setPrimary(Boolean.FALSE);
        }

        @Override
        public BiPredicate<User.Role, User.Role> equalsPredicate() {
            return (role1, role2) -> (nullSafeEquals(role1.getValue(), role2.getType()) &&
                    nullSafeEquals(role1.getType(), role2.getType()));
        }
    }

    public static class SwitchX509CertificatePrimaryTask<T extends User> extends SwitchPrimaryTask<T, User.X509Certificate> {
        @Override
        protected Collection<User.X509Certificate> getSubjects(T resource) {
            return resource.getX509Certificates();
        }

        @Override
        protected Predicate<User.X509Certificate> primaryPredicate() {
            return User.X509Certificate::isPrimary;
        }

        @Override
        protected Consumer<User.X509Certificate> disablePrimary() {
            return x509Certificate -> x509Certificate.setPrimary(Boolean.FALSE);
        }

        @Override
        public BiPredicate<User.X509Certificate, User.X509Certificate> equalsPredicate() {
            return (x5091, x5092) -> (nullSafeEquals(x5091.getValue(), x5092.getValue()) &&
                    nullSafeEquals(x5091.getType(), x5092.getType()));
        }
    }
}
