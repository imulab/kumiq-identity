package com.kumiq.identity.scim.endpoint;

import com.kumiq.identity.scim.utils.ExceptionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
@ControllerAdvice
public class ExceptionResolver {

    private static final Logger log = LoggerFactory.getLogger(ExceptionResolver.class);

    @ExceptionHandler(ExceptionFactory.ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleResourceNotFoundException(ExceptionFactory.ResourceNotFoundException ex) {
        log.error(String.format("Resource [%s(%s)] was not found.", ex.getResourceType(), ex.getResourceId()));
    }
}
