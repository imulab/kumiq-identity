package com.kumiq.identity.scim.utils;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class ETagUtils {

    public static String createWeakEtag(Long resourceVersion) {
        return "W/\"" + resourceVersion + "\"";
    }
}
