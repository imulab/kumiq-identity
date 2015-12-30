## Hint Schema

Together from the defined `Schema` resources, KUMIQ Identity uses `Hint Schema` to assist interpretation of resource attribute types, constraints and other information.

A `Hint Schema` is just a ordinary schema resource with the following exception:
- It is a stand alone schema for the resource defined by the app, meaning it includes the `core schema` (e.g. `id`, `externalId`, `meta`)
- It defines a `class` attribute, identifying the Java/Groovy class used to model the attribute
- It defines a `elementClass` attribute, identifying the Java/Groovy class of its element, in the case that the attribute is multivalued
- It defines a `property` attribute, identifying the name of the attribute in the Java/Groovy class for the corresponding attribute. (versus the `name` attribute, which actually defines the JSON API key). When empty, it defaults to the `name` attribute.
- It can be queried in the application by special id, namely `HintUser` and `HintGroup`.
- It is excluded from the calculation in the schema API.
