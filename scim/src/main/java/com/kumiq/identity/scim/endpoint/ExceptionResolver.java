package com.kumiq.identity.scim.endpoint;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kumiq.identity.scim.exception.ApiException;
import com.kumiq.identity.scim.utils.JsonDateToUnixTimestampSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
@ControllerAdvice
public class ExceptionResolver {

    private static final Logger log = LoggerFactory.getLogger(ExceptionResolver.class);

//    @ExceptionHandler(ExceptionFactory.ResourceNotFoundException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public void handleResourceNotFoundException(ExceptionFactory.ResourceNotFoundException ex) {
//        log.error(String.format("Resource [%s(%s)] was not found.", ex.getResourceType(), ex.getResourceId()));
//    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleAllExceptions(Exception ex, HttpServletResponse response) {
        if (ex instanceof ApiException)
            return handleApiException((ApiException) ex, response);
        return handleGenericException(ex, response);
    }

    public ErrorResponse handleApiException(ApiException ex, HttpServletResponse response) {
        log.error(ex.defaultMessage());
        response.setStatus(ex.httpStatus().value());
        return ErrorResponse.fromApiException(ex);
    }

    public ErrorResponse handleGenericException(Exception ex, HttpServletResponse response) {
        log.error(ex.getLocalizedMessage());
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ErrorResponse.fromGenericException(ex);
    }

    public static class ErrorResponse {

        @JsonProperty("error")
        private String errorName;

        @JsonProperty("time")
        @JsonSerialize(using = JsonDateToUnixTimestampSerializer.class)
        private Date errorTime;

        @JsonProperty("message")
        public String message;

        @JsonProperty("statusCode")
        public int statusCode;

        public static ErrorResponse fromApiException(ApiException ex) {
            ErrorResponse response = new ErrorResponse();
            response.errorName = ex.getClass().getSimpleName();
            response.errorTime = new Date();
            response.message = ex.defaultMessage();
            response.statusCode = ex.httpStatus().value();
            return response;
        }

        public static ErrorResponse fromGenericException(Exception ex) {
            ErrorResponse response = new ErrorResponse();
            response.errorName = "GenericException";
            response.errorTime = new Date();
            response.message = ex.getLocalizedMessage();
            response.statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
            return response;
        }

        public String getErrorName() {
            return errorName;
        }

        public void setErrorName(String errorName) {
            this.errorName = errorName;
        }

        public Date getErrorTime() {
            return errorTime;
        }

        public void setErrorTime(Date errorTime) {
            this.errorTime = errorTime;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }
    }
}
