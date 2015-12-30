## Path Compiler

A `PathCompiler` takes in `SCIM path w/wo filter`, `resource data`, `hint schema`, `configuration`s and produces one or more definite path linked-lists represented by the head token in the form of `PathRef`. A subsequent component can use the result to evaluate or modify the resource data.

First, it uses `PathTokenizer` to break down scim paths down to tokens. Each tokens are parsed by the `PathTokenFactory` to either `SimplePathToken` or `PathTokenWithFilter`. For example: `emails[primary eq true].value` is parsed to `PathTokenWithFilter` of `emails[primary eq true]` and `SimplePathToken` of `value`. And of course, there is a `PathRoot` in front of them.

Second, it would traverse the tree (in the first run, the tree is effectively still a linked-list) and detect any `expandable tokens`. There are two cases where a token is considered expandable:
- If the compiler relies on `Hint Schema`, (option `COMPILE_WITH_HINT` is on in configurations), a path token is expandable if the corresponding attribute for its path is multivalued.
- If the compiler does not rely on `Hint Schema`, a path token is considered expandable if it is a `PathTokenWithFilter`.

The compiler triggers an `replacement process` for any expandable token detected.

A `replacement process` attempts to evaluate the expandable token and make it definite by calculating the indexes which would satisfy the filter condition:
- For a multivalued `PathTokenWithFilter`, the filter is compiled by the `FilterCompiler`. Each element would be filtered by the `Predicate` provided by the `FilterCompiler`  to ensure the indexes.
- For a multivalued `SimplePathToken`, it is evaluated and all indexes would be extracted.

After getting a list of `replacement tokens`, the original expandable token is swapped with the tokens in the list. The detection process restarts with each traversable paths after that.

Finally, the detection process produces a tree consisting of only `PathRoot`, `SimplePathToken` and `PathTokenWithIndex`. The tree would be traversed and each node would be wrapped in `PathRef`. The head of each `PathRef` list would be returned.
