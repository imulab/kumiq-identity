package com.kumiq.identity.scim.bulk;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumiq.identity.scim.endpoint.support.BulkOpRequest;
import com.kumiq.identity.scim.endpoint.support.BulkOpResponse;
import com.kumiq.identity.scim.exception.ExceptionFactory;
import com.kumiq.identity.scim.resource.user.User;
import com.kumiq.identity.scim.task.UserReplaceContext;
import com.kumiq.identity.scim.task.shared.CheckVersionTask;
import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.PathMatcher;

import java.util.Map;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
@SuppressWarnings("unchecked")
public class ReplaceUserBulkOperationExecutor extends CommonBulkOperationExecutor<UserReplaceContext> {

    private static final String PATH_PATTERN = "/Users/*";
    private ObjectMapper objectMapper = new ObjectMapper();
    private Class<? extends User> userClazz;
    private PathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected UserReplaceContext createOperationContext(BulkOpRequest.Operation operation) {
        UserReplaceContext context = new UserReplaceContext();
        context.setId(pathMatcher.extractPathWithinPattern(PATH_PATTERN, operation.getPath()));
        context.setResource(convertJsonDataToUser(operation.getJsonData()));
        context.getUserInfo().put(CheckVersionTask.VERSION_KEY, operation.getVersion());
        return context;
    }

    @Override
    protected void handleSuccessfulExecution(UserReplaceContext context, BulkOpResponse.Operation response) {
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
        return HttpMethod.PUT.equals(operation.getMethod()) && pathMatcher.match(PATH_PATTERN, operation.getPath());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        Assert.notNull(pathMatcher);
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

    public PathMatcher getPathMatcher() {
        return pathMatcher;
    }

    public void setPathMatcher(PathMatcher pathMatcher) {
        this.pathMatcher = pathMatcher;
    }
}
