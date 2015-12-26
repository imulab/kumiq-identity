package com.kumiq.identity.scim.bulk;

import com.kumiq.identity.scim.endpoint.support.BulkOpRequest;
import com.kumiq.identity.scim.endpoint.support.BulkOpResponse;
import com.kumiq.identity.scim.exception.ExceptionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class DelegatingBulkOperationExecutor implements BulkOperationExecutor, InitializingBean {

    private List<BulkOperationExecutor> executors;

    @Override
    public BulkOpResponse.Operation execute(BulkOpRequest.Operation operation) {
        Optional<BulkOperationExecutor> executor = executors
                .stream()
                .filter(bulkOperationExecutor -> bulkOperationExecutor.supports(operation))
                .findFirst();
        if (!executor.isPresent())
            throw ExceptionFactory.fail("Unsupported bulk operation.");

        return executor.get().execute(operation);
    }

    @Override
    public boolean supports(BulkOpRequest.Operation operation) {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.isTrue(!CollectionUtils.isEmpty(executors));
    }

    public List<BulkOperationExecutor> getExecutors() {
        return executors;
    }

    public void setExecutors(List<BulkOperationExecutor> executors) {
        this.executors = executors;
    }
}
