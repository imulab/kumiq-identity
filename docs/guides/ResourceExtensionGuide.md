## How to provide a resource extension?

This document explains the steps to provide a custom resource extension in `ScimUser` or `ScimGroup`. It uses `urn:ietf:params:scim:schemas:extension:enterprise:2.0:User` as an example.

First, define `Groovy Trait` in extension model package (i.e. `com.kumiq.identity.scim.resource.extension`). For the enterprise extension, we have class `EnterpriseExtension`.

Second, define a static inner class inside `EnterpriseExtension`, namely `Extension`. Provide all your properties/attributes in that class.

Third, in `ScimUser` class, implement `EnterpriseExtension` trait and reference the inner class as `EnterpriseExtension.Extension`.

Forth, provide detailed Hint Schema for the new extension.

Lastly, add the extension URN as keyword to the `PathTokenizer` if it contains period.
