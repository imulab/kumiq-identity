## Path Token

A `Path Token` is a representation of SCIM path pointing to a specific attribute. It is the base unit in compiler calculation. It is one previous `PathToken` and several next `PathToken`, therefore is capable of forming a tree. There are four types of `PathToken`s:
- `RootToken`: A void token placed in front of any path token trees or linked-lists.
- `SimplePathToken`: A token representing a path pointing to a single valued attribute in the resource or a multi valued attribute in the resource which is evaluated to be empty or null. (Consult configuration option: `TREAT_EMPTY_MULTIVALUE_AS_SIMPLE`)
- `PathTokenWithFilter`: A token representing zero or more paths pointing to a subset of values from the multivalued attribute in resource. It carries information about how to filter the collection of values. It is parsed from tokens like `emails[value sw "ABC"]`
- `PathTokenWithIndex`: A token representing a definite path pointing to a specific value in the multivalued attribute. It is the result of evaluation from `PathTokenWithFilter`. Normally, `Path Compiler` takes a `PathTokenWithFilter` and replace it with 1 or more `PathTokenWithIndex` or a `SimplePathToken` in case the collection has no elements.
