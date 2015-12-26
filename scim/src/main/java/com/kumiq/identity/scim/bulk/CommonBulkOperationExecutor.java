package com.kumiq.identity.scim.bulk;

import com.kumiq.identity.scim.endpoint.ExceptionResolver;
import com.kumiq.identity.scim.endpoint.support.BulkOpRequest;
import com.kumiq.identity.scim.endpoint.support.BulkOpResponse;
import com.kumiq.identity.scim.exception.ApiException;
import com.kumiq.identity.scim.task.ResourceOpContext;
import com.kumiq.identity.scim.task.Task;
import com.kumiq.identity.scim.task.shared.PopulateETagHeaderTask;
import com.kumiq.identity.scim.task.shared.PopulateLocationHeaderTask;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public abstract class CommonBulkOperationExecutor<T extends ResourceOpContext> implements BulkOperationExecutor, InitializingBean {

    protected Task<T> task;

    @Override
    public BulkOpResponse.Operation execute(BulkOpRequest.Operation operation) {
        if (!supports(operation))
            throw new IllegalArgumentException("Not supported! Operation shouldn't have been dispatched to this executor");

        T operationContext = createOperationContext(operation);
        BulkOpResponse.Operation response = initializeResponse(operation);

        try {
            task.perform(operationContext);
            handleSuccessfulExecution(operationContext, response);
        } catch (Exception ex) {
            handleErrorExecution(ex, response);
        }

        return response;
    }

    protected HttpStatus successHttpStatus() {
        return HttpStatus.OK;
    }

    protected BulkOpResponse.Operation initializeResponse(BulkOpRequest.Operation operation) {
        BulkOpResponse.Operation response = new BulkOpResponse.Operation();
        response.setMethod(operation.getMethod());
        response.setBulkId(operation.getBulkId());
        response.setVersion(operation.getVersion());
        return response;
    }

    abstract protected T createOperationContext(BulkOpRequest.Operation operation);

    protected void handleSuccessfulExecution(T context, BulkOpResponse.Operation response) {
        response.setHttpStatus(successHttpStatus());
        if (context.getUserInfo().containsKey(PopulateETagHeaderTask.ETAG))
            response.setVersion(context.getUserInfo().get(PopulateETagHeaderTask.ETAG).toString());
        if (context.getUserInfo().containsKey(PopulateLocationHeaderTask.LOCATION))
            response.setLocation(context.getUserInfo().get(PopulateLocationHeaderTask.LOCATION).toString());
    }

    protected void handleErrorExecution(Exception exception, BulkOpResponse.Operation response) {
        ExceptionResolver.ErrorResponse errorResponse;
        if (exception instanceof ApiException) {
            errorResponse = ExceptionResolver.ErrorResponse.fromApiException((ApiException) exception);
        } else {
            errorResponse = ExceptionResolver.ErrorResponse.fromGenericException(exception);
        }
        response.setResponse(errorResponse);
        response.setHttpStatus(errorResponse.getStatusCode());
    }

    public Task<T> getTask() {
        return task;
    }

    public void setTask(Task<T> task) {
        this.task = task;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(task);
    }
}
