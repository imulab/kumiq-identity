package com.kumiq.identity.scim.bulk;

import com.kumiq.identity.scim.endpoint.support.BulkOpRequest;
import com.kumiq.identity.scim.endpoint.support.BulkOpResponse;
import com.kumiq.identity.scim.task.GroupDeleteContext;
import com.kumiq.identity.scim.task.ResourceOpContext;
import com.kumiq.identity.scim.task.shared.CheckVersionTask;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.PathMatcher;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
@SuppressWarnings("unchecked")
public class DeleteGroupBulkOperationExecutor extends CommonBulkOperationExecutor<GroupDeleteContext> {

    private static final String PATH_PATTERN = "/Groups/*";

    private PathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected GroupDeleteContext createOperationContext(BulkOpRequest.Operation operation) {
        GroupDeleteContext context = new GroupDeleteContext();
        context.setId(pathMatcher.extractPathWithinPattern(PATH_PATTERN, operation.getPath()));
        context.getUserInfo().put(CheckVersionTask.VERSION_KEY, operation.getVersion());
        return context;
    }

    @Override
    protected HttpStatus successHttpStatus() {
        return HttpStatus.NO_CONTENT;
    }

    @Override
    public boolean supports(BulkOpRequest.Operation operation) {
        return HttpMethod.DELETE.equals(operation.getMethod()) && pathMatcher.match(PATH_PATTERN, operation.getPath());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        Assert.notNull(pathMatcher);
    }

    public PathMatcher getPathMatcher() {
        return pathMatcher;
    }

    public void setPathMatcher(PathMatcher pathMatcher) {
        this.pathMatcher = pathMatcher;
    }
}
