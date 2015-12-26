package com.kumiq.identity.scim.endpoint;

import com.kumiq.identity.scim.bulk.BulkOpRequestBodyValidator;
import com.kumiq.identity.scim.bulk.BulkOperationExecutor;
import com.kumiq.identity.scim.endpoint.support.BulkOpRequest;
import com.kumiq.identity.scim.endpoint.support.BulkOpResponse;
import com.kumiq.identity.scim.exception.ExceptionFactory;
import com.kumiq.identity.scim.resource.constant.ScimConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
@RestController
@RequestMapping("/Bulk")
public class BulkEndpoints {

    @Autowired
    private BulkOpRequestBodyValidator requestBodyValidator;

    @Autowired
    private MessageSource messageSource;

    @Resource(name = "bulkOperationExecutor")
    BulkOperationExecutor executor;

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(requestBodyValidator);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = ScimConstants.SCIM_CONTENT_TYPE, produces = ScimConstants.SCIM_CONTENT_TYPE)
    public BulkOpResponse bulkOperation(@RequestBody @Valid BulkOpRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors()
                    .stream()
                    .map(objectError ->
                                    messageSource.getMessage(
                                            objectError.getCode(),
                                            objectError.getArguments(),
                                            objectError.getDefaultMessage(),
                                            LocaleContextHolder.getLocale()))
                    .collect(Collectors.toList());
            Map<String, Object> details = new HashMap<>();
            details.put("Errors", errorMessages);
            throw new ExceptionFactory.InvalidRequestBodyException(details);
        }

        BulkOpResponse response = new BulkOpResponse();
        response.setOperations(new ArrayList<>());
        int errorCount = 0;
        for (BulkOpRequest.Operation operation : request.getOperations()) {
            BulkOpResponse.Operation responseUnit = executor.execute(operation);
            if (responseUnit.getHttpStatus().is4xxClientError() || responseUnit.getHttpStatus().is5xxServerError())
                errorCount++;
            if (errorCount >= request.getFailOnErrors())
                break;
            response.getOperations().add(responseUnit);
        }

        return response;
    }

    public BulkOpRequestBodyValidator getRequestBodyValidator() {
        return requestBodyValidator;
    }

    public void setRequestBodyValidator(BulkOpRequestBodyValidator requestBodyValidator) {
        this.requestBodyValidator = requestBodyValidator;
    }

    public MessageSource getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
