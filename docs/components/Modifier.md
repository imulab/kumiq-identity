## Modifier

A `Modifier` is responsible of handling a `ModificationUnit` of either `add`, `replace` or `remove`. Correspondingly, there are three kinds of `Modifier`: `AddModifier`, `ReplaceModifier` and `RemoveModifier`.

Each `Modifier` uses `PathCompiler` to compile the `path` and evaluate the path list to the last node and then perform corresponding modifications on the last node.

Sometimes, an intermediate node does not exist already. For a `AddModifier`, it would try to create it according to the `class` attribute defined in the schema. The other two `Modifier` would abort.

When faced with a multivalued property, an `AddModifier` can handle insertion of both multivalued objects and single valued objects.

When single valued attribute already contains value, operation with an `AddModifier` have same effect with `ReplaceModifier`.

`RemoveModifier` deletes data, hence it does not require and data input. Passing `null` for data will suffice.
