package com.kumiq.identity.scim.path;

import com.kumiq.identity.scim.spi.MapObjectProvider;
import com.kumiq.identity.scim.spi.ObjectProvider;

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

    public Configuration withOption(Option option) {
        this.options.add(option);
        return this;
    }

    private Configuration(final ObjectProvider objectProvider) {
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
    }
}
