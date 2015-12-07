package com.kumiq.identity.scim.spi;

import java.util.Collection;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public interface ObjectProvider {

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
}
