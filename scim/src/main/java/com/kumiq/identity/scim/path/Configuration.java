package com.kumiq.identity.scim.path;

import com.kumiq.identity.scim.spi.MapObjectProvider;
import com.kumiq.identity.scim.spi.ObjectProvider;
import com.kumiq.identity.scim.spi.ResourceObjectProvider;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class Configuration {

    private final ObjectProvider objectProvider;

    private Set<Option> options = new HashSet<>();

    public static Configuration withMapObjectProvider() {
        return new Configuration(new MapObjectProvider());
    }

    public static Configuration withResourceObjectProvider() {
        return new Configuration(new ResourceObjectProvider());
    }

    public Configuration withOption(Option option) {
        this.options.add(option);
        return this;
    }

    public Configuration(final ObjectProvider objectProvider) {
        this.objectProvider = objectProvider;
    }

    public ObjectProvider getObjectProvider() {
        return objectProvider;
    }

    public Set<Option> getOptions() {
        return options;
    }

    public void setOptions(Set<Option> options) {
        this.options = options;
    }

    public Configuration clone() {
        Configuration clone = new Configuration(this.objectProvider);
        clone.setOptions(this.getOptions());
        return clone;
    }

    public enum Option {

        /**
         * Suppress any exception during evaluation or modification.
         */
        SUPPRESS_EXCEPTION,

        /**
         * Require compiler to operate with Schema hint for multiValue information.
         * By default, it is not set, and compiler will not fail if attribute information
         * is not found by path.
         * When set, compiler would rely on schema and attribute to do work. And will attempt
         * to throw exception if an attribute is not found by path. Whether or not an exception
         * is thrown at the end still depends on {@code SUPPRESS_EXCEPTION}.
         */
        COMPILE_WITH_HINT,

        /**
         * Use API attribute name over the model attribute name in actions like modifying. Useful
         * when the {@link Modifier} is used in conjunction with {@link ResourceMapper} when the API
         * attribute name is preferred to be used as keys in rendering. When this options is not set,
         * model attribute name will still be used.
         */
        API_ATTR_NAME_PREF,

        /**
         * Inform the evaluation context that evaluated to null because of premature null hit.
         */
        INFORM_PREMATURE_EXIT,
    }
}
