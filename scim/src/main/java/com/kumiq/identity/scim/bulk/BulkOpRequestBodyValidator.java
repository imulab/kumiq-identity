package com.kumiq.identity.scim.bulk;

import com.kumiq.identity.scim.endpoint.support.BulkOpRequest;
import com.kumiq.identity.scim.resource.constant.ScimConstants;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
@Component
public class BulkOpRequestBodyValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return BulkOpRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (((BulkOpRequest) target).getSchemas() == null ||
                !((BulkOpRequest) target).getSchemas().contains(ScimConstants.URN_BULK_REQUEST)) {
            errors.reject("error.bulk.wrongSchema",
                    new Object[]{},
                    "Wrong schema for bulk request");
            return;
        }

        IntStream.range(0, ((BulkOpRequest) target).getOperations().size()).forEach(value -> {
            BulkOpRequest.Operation operation = ((BulkOpRequest) target).getOperations().get(value);

            if (!Arrays.asList(
                    HttpMethod.POST,
                    HttpMethod.PUT,
                    HttpMethod.PATCH,
                    HttpMethod.DELETE).contains(operation.getMethod()))
                errors.reject("error.bulk.unsupportedMethod",
                        new Object[]{value, operation.getMethod()},
                        operation.getMethod() + " is not supported");

            if (HttpMethod.POST.equals(operation.getMethod()) && !StringUtils.hasLength(operation.getBulkId()))
                errors.reject("error.bulk.missingBulkId",
                        new Object[]{value},
                        "Missing bulk id");

            if (Arrays.asList(
                    HttpMethod.PUT,
                    HttpMethod.PATCH,
                    HttpMethod.DELETE).contains(operation.getMethod()) && !StringUtils.hasLength(operation.getVersion()))
                errors.reject("error.bulk.missingVersion",
                        new Object[]{value},
                        "Missing version");

            if (!StringUtils.hasLength(operation.getPath()))
                errors.reject("error.bulk.missingPath",
                        new Object[]{value},
                        "Missing path");

            if (Arrays.asList(
                    HttpMethod.POST,
                    HttpMethod.PUT,
                    HttpMethod.PATCH).contains(operation.getMethod()) && CollectionUtils.isEmpty(operation.getJsonData()))
                errors.reject("error.bulk.missingData",
                        new Object[]{value},
                        "Missing data");
        });
    }
}
