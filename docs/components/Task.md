## Task

A `Task` is a stateless single unit of action about a state-ful `ResourceOpContext`. It is the main piece of work for most resource related operations.

A `SimpleTaskChain` is a special type of `Task` which facilitates a list of `Task` to perform in sequence. It accumulates effects to the `ResourceOpContext` of each housing `Task` and achieves a more complex effect. It also promotes modularity and making adding or removing logic much easier, hence more maintainable.

For example: the process of creating a `User` is a `SimpleTaskChain` consisting of the following `Task`s:
- Set hint schema
- Ignore read only attributes in the request body
- Assign new UUID to the id field
- Create the meta section
- Check all required fields are present
- Check all unique fields are indeed unique
- Check all reference fields do point to an existing resource
- Check all attributes which has a `primary` field (e.g. emails, addresses) only contain zero or one element marked as `primary`.
- Save to database
- Perform side effect: update membership for the created user across its provided groups.
- Populate `ETag` information
- Populate `Location` information
