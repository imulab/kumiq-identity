# KUMIQ Identity

## Shameless Plug before Everything Else

If you are interested in SCIM v2 implemention in Go, please check out [go-scim](https://github.com/davidiamyou/go-scim)

## Overview

The KUMIQ Identity project is an implementation of [SCIM v2.0](http://www.simplecloud.info) by Weinan Qiu. It supports most of the features specified in the documentation and provides an easy way to add custom extensions to the resource schema.

## Features

- [x] Resource endpoints
- [x] Extension capability
- [x] Query on `User` and `Group`
- [ ] Query on root
- [x] Patch resource
- [x] Bulk operation
- [x] Filter resource
- [x] Sort resource
- [x] ETag version check
- [x] Change password: _Available through replace and patch, does not have separate endpoint yet_
- [ ] Security (Authentication): _Planned with OAuth, to be provided by another project_
- [x] In memory database
- [ ] JDBC database: _Planned on roadmap_
- [ ] Mongo database: _Planned on roadmap_
- [x] Stock resource bootstrap on startup

## Build & Run

KUMIQ Identity is a straightforward spring-boot project based on **Apache Maven** so all of the usual build commands apply.

For a quick start:

```
cd scim
mvn clean spring-boot:run
```

_Note the project structures will change after v0.1 as we introduce SQL and/or MongoDB as persistence stores._

## Components

The section introduces some of the details of the key components the project relies on.

- [Resource](docs/components/Resource.md)
- [Hint Schema](docs/components/HintSchema.md)
- [Database & Init](docs/components/DatabaseAndInit.md)
- [Tokenizer](docs/components/Tokenizer.md)
- [PathToken](docs/components/PathToken.md)
- [PathRef](docs/components/PathRef.md)
- [Path Compiler](docs/components/PathCompiler.md)
- [Filter Compiler](docs/components/FilterCompiler.md)
- [Modifier](docs/components/Modifier.md)
- [ResourceMapper](docs/components/ResourceMapper.md)
- [Task](docs/components/Task.md)
- [Bulk Operation Executor](docs/components/BulkOpExecutor.md)

## Guides

- [How to provide resource extension?](docs/guides/ResourceExtensionGuide.md)
- [Configuration options explained](docs/guides/ConfigurationOptionsGuide.md)

## Mention

Some of the components in the compiler and evaluator are inspired by [Jayway's JsonPath](https://github.com/jayway/JsonPath).

## License

KUMIQ Identity project is under MIT License.
