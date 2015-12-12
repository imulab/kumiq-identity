package com.kumiq.identity.scim.spi;

import com.kumiq.identity.scim.resource.misc.Schema;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.util.Assert;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.stream.IntStream;

import static com.kumiq.identity.scim.utils.TypeUtils.asList;
import static com.kumiq.identity.scim.utils.TypeUtils.isList;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
@SuppressWarnings("unchecked")
public class ResourceObjectProvider implements ObjectProvider {

    @Override
    public boolean containsKey(Object obj, String key) {
        return getPropertyValue(obj, key) != null;
    }

    @Override
    public Collection<String> getPropertyKeys(Object obj) {
        try {
            return BeanUtils.describe(obj).keySet();
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Object createArrayElement(String key, Schema.Attribute hint) {
        Assert.notNull(hint.getElementClazz(), "Cannot create array element without element class hint");
        return newObjectFromClass(hint.getElementClazz());
    }

    @Override
    public Object getArrayIndex(Object obj, int idx) {
        Assert.isTrue(isList(obj), "only list is supported");
        return asList(obj).get(idx);
    }

    @Override
    public void setArrayIndex(Object array, int idx, Object newValue) {
        Assert.isTrue(isList(array), "only list is supported");
        int size = asList(array).size();

        if (idx - (size - 1) > 0) {
            IntStream.range(0, (idx - (size - 1))).forEach(value -> asList(array).add(newObjectFromClass(newValue.getClass())));
        }

        asList(array).set(idx, newValue);
    }

    @Override
    public void addToArray(Object array, Collection<Object> newValues) {
        Assert.isTrue(isList(array), "only list is supported");
        asList(array).addAll(newValues);
    }

    @Override
    public void removeFromArray(Object array, int idx) {
        Assert.isTrue(isList(array), "only list is supported");
        asList(array).remove(idx);
    }

    @Override
    public Object createObject(String key, Schema.Attribute hint) {
        Assert.notNull(hint.getClazz(), "Cannot create object without class hint");
        return newObjectFromClass(hint.getClazz());
    }

    @Override
    public Object getPropertyValue(Object obj, String key) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass(), Object.class);
            for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
                if (descriptor.getName().equals(key))
                    return descriptor.getReadMethod().invoke(obj);
            }
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }

        throw new RuntimeException("No such key [" + key + "] in object [" + obj.toString() + "]");
    }

    @Override
    public void setPropertyValue(Object obj, String key, Object newValue) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass(), Object.class);
            for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
                if (descriptor.getName().equals(key)) {
                    descriptor.getWriteMethod().invoke(obj, newValue);
                    return;
                }
            }
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }

        throw new RuntimeException("No such key [" + key + "] in object");
    }

    @Override
    public void removePropertyValue(Object obj, String key) {
        setPropertyValue(obj, key, null);
    }

    @Override
    public void createMissingPropertyStructure(Object obj, String key, Schema.Attribute hint) {
        if (getPropertyValue(obj, key) != null)
            return;

        Assert.isTrue(key.equals(hint.getName()));
        setPropertyValue(obj, key, createObject(key, hint));
    }

    private Object newObjectFromClass(Class clazz) {
        if (clazz.equals(Boolean.class))
            return Boolean.FALSE;
        else if (clazz.equals(Long.class))
            return 0l;
        else if (clazz.equals(Integer.class))
            return 0;

        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }
}
