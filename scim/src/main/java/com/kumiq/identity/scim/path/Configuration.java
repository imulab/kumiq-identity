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
        SUPPRESS_EXCEPTION


    }
}
