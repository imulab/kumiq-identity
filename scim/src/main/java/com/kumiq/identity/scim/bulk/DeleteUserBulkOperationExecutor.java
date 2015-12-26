package com.kumiq.identity.scim.bulk;

import com.kumiq.identity.scim.endpoint.support.BulkOpRequest;
import com.kumiq.identity.scim.task.UserDeleteContext;
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
public class DeleteUserBulkOperationExecutor extends CommonBulkOperationExecutor<UserDeleteContext> {

    private static final String PATH_PATTERN = "/Users/*";

    private PathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected UserDeleteContext createOperationContext(BulkOpRequest.Operation operation) {
        UserDeleteContext context = new UserDeleteContext();
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
}
