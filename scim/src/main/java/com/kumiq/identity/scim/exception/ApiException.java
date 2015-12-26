package com.kumiq.identity.scim.exception;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides exception information which could be rendering in the API.
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
public interface ApiException {

    /**
     * Http status code
     *
     * @return
     */
    HttpStatus httpStatus();

    /**
     * Code for message resource bundle
     *
     * @return
     */
    String messageCode();

    /**
     * Arguments
     * @return
     */
    Object[] messageArgs();

    /**
     * Default message that will be used in case message code is not found.
     *
     * @return
     */
    String defaultMessage();

    /**
     * Extra error information
     *
     * @return
     */
    default Map<String, Object> userInfo() {
        return new HashMap<>();
    }
}
