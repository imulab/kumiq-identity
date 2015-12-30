## Configuration Options

This document explains each configuration options:
- `SUPPRESS_EXCEPTION`: suppress exception during compilation. Sometimes we need the compiler to just silently fail.
- `COMPILE_WITH_HINT`: tell the compiler to use `Hint Schema` when it tries to make decisions
- `API_ATTR_NAME_PREF`: Preference of API attribute over model class field names. This is useful in `ResourceMapper` when it needs to use the API JSON field name as keys when creating map entries.
- `INFORM_PREMATURE_EXIT`: Tell the evaluation consumer that the reason for the evaluation result to be `null` is because an intermediate node evaluated to `null`. In some case, we need to know whether the tail node evaluated to null or the evaluation process didn't even reach there.
- `TREAT_EMPTY_MULTIVALUE_AS_SIMPLE`: Tell the compiler to treat multivalued attribute that has no data elements as `SimplePathToken` instead of terminating the compilation process.
