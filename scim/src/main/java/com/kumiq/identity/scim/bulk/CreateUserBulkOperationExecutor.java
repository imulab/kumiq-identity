package com.kumiq.identity.scim.bulk;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumiq.identity.scim.endpoint.support.BulkOpRequest;
import com.kumiq.identity.scim.endpoint.support.BulkOpResponse;
import com.kumiq.identity.scim.exception.ExceptionFactory;
import com.kumiq.identity.scim.resource.user.User;
import com.kumiq.identity.scim.task.UserCreateContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
@SuppressWarnings("unchecked")
public class CreateUserBulkOperationExecutor extends CommonBulkOperationExecutor<UserCreateContext> {

    private ObjectMapper objectMapper = new ObjectMapper();
    private Class<? extends User> userClazz;

    @Override
    protected UserCreateContext createOperationContext(BulkOpRequest.Operation operation) {
        UserCreateContext context = new UserCreateContext();
        context.setResource(convertJsonDataToUser(operation.getJsonData()));
        return context;
    }

    @Override
    protected HttpStatus successHttpStatus() {
        return HttpStatus.CREATED;
    }

    @Override
    protected void handleSuccessfulExecution(UserCreateContext context, BulkOpResponse.Operation response) {
        super.handleSuccessfulExecution(context, response);
        response.setResponse(context.getResource());
    }

    private User convertJsonDataToUser(Map jsonData) {
        try {
            String rawJson = objectMapper.writeValueAsString(jsonData);
            return objectMapper.readValue(rawJson, userClazz);
        } catch (Exception ex) {
            throw new ExceptionFactory.InvalidRequestBodyException();
        }
    }

    @Override
    public boolean supports(BulkOpRequest.Operation operation) {
        return HttpMethod.POST.equals(operation.getMethod()) && "/Users".equals(operation.getPath());
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Class<? extends User> getUserClazz() {
        return userClazz;
    }

    public void setUserClazz(Class<? extends User> userClazz) {
        this.userClazz = userClazz;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        Assert.notNull(objectMapper);
        Assert.notNull(userClazz);
    }
}
