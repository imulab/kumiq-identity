## Bulk Operation Executor

A `BulkOperationExecutor` is a wrapper around the main `SimpleTaskChain` for a resource operation. It is heavily used in `Bulk` operation.

Its main duty is to:
- Prepare the specific `ResourceOpContext` from the provided `BulkOpRequest.Operation`
- Trigger the task execution
- Interpret the result of execution and prepare `BulkOpResponse.Operation` for return

It is also aware of whether itself can handle the `BulkOpRequest.Operation` through the `supports(BulkOpRequest.Operation)` method.

The `DelegatingBulkOperationExecutor` contains all `BulkOperationExecutor` and delegate the work to the specific executor that can handle the input. Therefore, it provides a single point of access for service consumers for bulk operation.
