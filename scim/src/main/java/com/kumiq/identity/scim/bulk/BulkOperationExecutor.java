package com.kumiq.identity.scim.bulk;

import com.kumiq.identity.scim.endpoint.support.BulkOpRequest;
import com.kumiq.identity.scim.endpoint.support.BulkOpResponse;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public interface BulkOperationExecutor {

    BulkOpResponse.Operation execute(BulkOpRequest.Operation operation);

    boolean supports(BulkOpRequest.Operation operation);
}
