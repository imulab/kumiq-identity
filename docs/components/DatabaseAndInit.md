## Database and Init

The database layer is responsible for data exchange with the underlying persistence provider. It provides interfaces which specific persistence technologies can implement. By default, KUMIQ Identity provides a in memory database implementation and they are automatically configured.

Thanks to `Spring Boot`'s `@ConditionalOnMissingBean` annotation, users can provide custom implementations conforming to `UserDatabase` and/or `GroupDatabase` and define them as beans to swap out the in memory implementation. As for the other database, namely `ResourceDatabase`, `SchemaDatabase` and `ServiceProviderConfigDatabase`, it is recommended to leave the original in memory implementation as is.

`JDBC` implementation and `MongoDB` implementation is on the roadmap.

On startup, the application is configured to read JSON files at `classpath:stock/**`. Stock resources provided in this way will be deserialized and persisted into database (existance checked).
