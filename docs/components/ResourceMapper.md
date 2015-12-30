## Resource Mapper

A `ResourceMapper` takes in `data`, `HintSchema`, `inclusion paths` and `exclusion paths` and provides a map containing all the paths that should be included. This is useful when `attributes` properties is provided in the API request so that only a subset of all attributes are expected in server response.

First, the provided `inclusion paths` and `exclusion paths` would be checked and updated:
- paths whose `returned` is `always` but absent from `inclusion paths` will be added
- paths whose `returned` is `never` but absent from `exclusion paths` will be added
- paths whose `returned` is `always` but present in `exclusion paths` will be removed
- paths whose `returned` is `never` but present in `inclusion paths` will be removed

After that, the `ResourceMapper` would use `PathCompiler` to compile each `inclusion paths` and evaluate the value. Then use the `AddModifier` to put values into the map.
