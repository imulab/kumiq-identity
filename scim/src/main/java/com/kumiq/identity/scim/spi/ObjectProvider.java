package com.kumiq.identity.scim.spi;

import com.kumiq.identity.scim.resource.misc.Schema;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public interface ObjectProvider {

    /**
     * Test if the given object has the key / property
     *
     * @param obj
     * @param key
     * @return
     */
    default boolean containsKey(Object obj, String key) {
        return getPropertyKeys(obj).contains(key);
    }

    /**
     * Return keys / properties from the given object
     *
     * @param obj
     * @return
     */
    Collection<String> getPropertyKeys(Object obj);

    /**
     * Extract value from an array
     *
     * @param obj
     * @param idx
     * @return
     */
    Object getArrayIndex(Object obj, int idx);

    /**
     * Set value in an index, if array is too small, provider should enlarge it.
     *
     * @param array
     * @param idx
     * @param newValue
     */
    void setArrayIndex(Object array, int idx, Object newValue);

    /**
     * Add value to the end of the array.
     *
     * @param array
     * @param newValues
     */
    void addToArray(Object array, Collection<Object> newValues);

    /**
     * Remove an element from array
     *
     * @param array
     * @param idx
     */
    void removeFromArray(Object array, int idx);

    /**
     * Extract a value from an object
     *
     * @param obj
     * @param key
     * @return
     */
    Object getPropertyValue(Object obj, String key);

    /**
     * Set value in object. If key does not exist, provider may throw exception
     *
     * @param obj
     * @param key
     * @param newValue
     */
    void setPropertyValue(Object obj, String key, Object newValue);

    /**
     * Remove value in object.
     *
     * @param obj
     * @param key
     */
    void removePropertyValue(Object obj, String key);

    /**
     * Create the structure for a missing property key with the help of attribute hint
     *
     * @param obj
     * @param key
     * @param hint
     */
    default void createMissingPropertyStructure(Object obj, String key, Schema.Attribute hint) {
        if (getPropertyValue(obj, key) != null)
            return;

        Assert.isTrue(key.equals(hint.getName()));
        Object newValue;
        if (hint.isMultiValued()) {
            newValue = new ArrayList<>();
        } else {
            try {
                newValue = hint.getClazz().getConstructor().newInstance();
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }
        setPropertyValue(obj, key, newValue);
    }
}
