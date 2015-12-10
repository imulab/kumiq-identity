package com.kumiq.identity.scim.spi;

import com.kumiq.identity.scim.resource.misc.Schema;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.stream.IntStream;

import static com.kumiq.identity.scim.utils.TypeUtils.*;

/**
 * An {@link ObjectProvider} that uses {@link java.util.Map} as its underlying data source
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
@SuppressWarnings(value = "unchecked")
public class MapObjectProvider implements ObjectProvider {

    @Override
    public boolean containsKey(Object obj, String key) {
        Assert.isTrue(isMap(obj), "only map is supported.");
        return getPropertyValue(obj, key) != null;
    }

    @Override
    public Collection<String> getPropertyKeys(Object obj) {
        Assert.isTrue(isMap(obj), "only map is supported.");
        return asMap(obj).keySet();
    }

    @Override
    public Object createArrayElement(String key, Schema.Attribute hint) {
        Assert.isTrue(hint.isMultiValued(), "Cannot create array element on single valued property");
        return new HashMap<>();
    }

    @Override
    public Object getArrayIndex(Object obj, int idx) {
        Assert.isTrue(isList(obj), "only list is supported");
        return asList(obj).get(idx);
    }

    @Override
    public void removeFromArray(Object array, int idx) {
        Assert.isTrue(isList(array), "only list is supported");
        asList(array).remove(idx);
    }

    @Override
    public void setArrayIndex(Object array, int idx, Object newValue) {
        Assert.isTrue(isList(array), "only list is supported");
        int size = asList(array).size();

        if (idx - (size - 1) > 0) {
            IntStream.range(0, (idx - (size - 1))).forEach(value -> asList(array).add(new HashMap<>()));
        }

        asList(array).set(idx, newValue);
    }

    @Override
    public void addToArray(Object array, Collection<Object> newValues) {
        Assert.isTrue(isList(array), "only list is supported");
        asList(array).addAll(newValues);
    }

    @Override
    public Object getPropertyValue(Object obj, String key) {
        Assert.isTrue(isMap(obj), "only map is supported.");
        return asMap(obj).get(key);
    }

    @Override
    public void setPropertyValue(Object obj, String key, Object newValue) {
        Assert.isTrue(isMap(obj), "only map is supported.");
        asMap(obj).put(key, newValue);
    }

    @Override
    public void removePropertyValue(Object obj, String key) {
        Assert.isTrue(isMap(obj), "only map is supported.");
        asMap(obj).remove(key);
    }

    @Override
    public Object createObject(String key, Schema.Attribute hint) {
        if (hint.isMultiValued()) {
            return new ArrayList<>();
        } else {
            return new HashMap<>();
        }
    }

    @Override
    public void createMissingPropertyStructure(Object obj, String key, Schema.Attribute hint) {
        if (getPropertyValue(obj, key) != null)
            return;

        Assert.isTrue(key.equals(hint.getName()));
        setPropertyValue(obj, key, createObject(key, hint));
    }
}
