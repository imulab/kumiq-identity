## Resource

KUMIQ Identity resources follows the 5 resources explained in the SCIM v2.0 specification:
- [User](https://tools.ietf.org/html/rfc7643#section-4.1)
- [Group](https://tools.ietf.org/html/rfc7643#section-4.2)
- [Schema](https://tools.ietf.org/html/rfc7643#section-7)
- [Resource Type](https://tools.ietf.org/html/rfc7643#section-6)
- [Service Provider Configuration](https://tools.ietf.org/html/rfc7643#section-5)

Among the 5 resources, `User` and `Group` can accept resource extensions, a.k.a., custom schema provided by the user. The `User` resource by default provides the [Enterprise User Extension](https://tools.ietf.org/html/rfc7643#section-4.3).
