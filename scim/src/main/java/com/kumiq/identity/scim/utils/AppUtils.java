package com.kumiq.identity.scim.utils;

import org.springframework.core.env.Environment;

import java.util.Arrays;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class AppUtils {

    public static boolean isTestProfileActive(Environment environment) {
        return isProfileActive(environment, "test");
    }

    public static boolean isProfileActive(Environment environment, String profile) {
        return Arrays.asList(environment.getActiveProfiles()).contains(profile);
    }
}
