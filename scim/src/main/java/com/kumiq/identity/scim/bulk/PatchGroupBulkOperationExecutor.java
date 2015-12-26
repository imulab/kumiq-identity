package com.kumiq.identity.scim.bulk;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumiq.identity.scim.endpoint.support.BulkOpRequest;
import com.kumiq.identity.scim.endpoint.support.PatchOpBody;
import com.kumiq.identity.scim.exception.ExceptionFactory;
import com.kumiq.identity.scim.task.GroupPatchContext;
import com.kumiq.identity.scim.task.shared.CheckVersionTask;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.Map;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
@SuppressWarnings("unchecked")
public class PatchGroupBulkOperationExecutor extends CommonBulkOperationExecutor<GroupPatchContext> {

    private static final String PATH_PATTERN = "/Groups/*";
    private ObjectMapper objectMapper = new ObjectMapper();
    private PathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected GroupPatchContext createOperationContext(BulkOpRequest.Operation operation) {
        GroupPatchContext context = new GroupPatchContext();
        context.setId(pathMatcher.extractPathWithinPattern(PATH_PATTERN, operation.getPath()));
        context.setModifications(convertJsonDataToBody(operation.getJsonData()).getOperations());
        context.getUserInfo().put(CheckVersionTask.VERSION_KEY, operation.getVersion());
        return context;
    }

    @Override
    protected HttpStatus successHttpStatus() {
        return HttpStatus.NO_CONTENT;
    }

    private PatchOpBody convertJsonDataToBody(Map jsonData) {
        try {
            String rawJson = objectMapper.writeValueAsString(jsonData);
            return objectMapper.readValue(rawJson, PatchOpBody.class);
        } catch (Exception ex) {
            throw new ExceptionFactory.InvalidRequestBodyException();
        }
    }

    @Override
    public boolean supports(BulkOpRequest.Operation operation) {
        return HttpMethod.PATCH.equals(operation.getMethod()) && pathMatcher.match(PATH_PATTERN, operation.getPath());
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public PathMatcher getPathMatcher() {
        return pathMatcher;
    }

    public void setPathMatcher(PathMatcher pathMatcher) {
        this.pathMatcher = pathMatcher;
    }
}
