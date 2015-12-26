package com.kumiq.identity.scim.bulk;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumiq.identity.scim.endpoint.support.BulkOpRequest;
import com.kumiq.identity.scim.endpoint.support.BulkOpResponse;
import com.kumiq.identity.scim.exception.ExceptionFactory;
import com.kumiq.identity.scim.resource.group.Group;
import com.kumiq.identity.scim.resource.user.User;
import com.kumiq.identity.scim.task.GroupCreateContext;
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
public class CreateGroupBulkOperationExecutor extends CommonBulkOperationExecutor<GroupCreateContext> {

    private ObjectMapper objectMapper = new ObjectMapper();
    private Class<? extends Group> groupClazz;

    @Override
    protected GroupCreateContext createOperationContext(BulkOpRequest.Operation operation) {
        GroupCreateContext context = new GroupCreateContext();
        context.setResource(convertJsonDataToGroup(operation.getJsonData()));
        return context;
    }

    @Override
    protected HttpStatus successHttpStatus() {
        return HttpStatus.CREATED;
    }

    @Override
    protected void handleSuccessfulExecution(GroupCreateContext context, BulkOpResponse.Operation response) {
        super.handleSuccessfulExecution(context, response);
        response.setResponse(context.getResource());
    }

    private Group convertJsonDataToGroup(Map jsonData) {
        try {
            String rawJson = objectMapper.writeValueAsString(jsonData);
            return objectMapper.readValue(rawJson, groupClazz);
        } catch (Exception ex) {
            throw new ExceptionFactory.InvalidRequestBodyException();
        }
    }

    @Override
    public boolean supports(BulkOpRequest.Operation operation) {
        return HttpMethod.POST.equals(operation.getMethod()) && "/Groups".equals(operation.getPath());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        Assert.notNull(objectMapper);
        Assert.notNull(groupClazz);
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Class<? extends Group> getGroupClazz() {
        return groupClazz;
    }

    public void setGroupClazz(Class<? extends Group> groupClazz) {
        this.groupClazz = groupClazz;
    }
}
