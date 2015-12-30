## Filter Compiler

A `FilterCompiler` takes in a `scim filter` and produces a `Predicate`.

First, it uses `FilterTokenizer` to break `scim filter` to tokens. Each token would be parsed by `FilterTokenFactory` to standard `FilterToken`.

After that, a [Shunting Yard Algorithm](https://en.wikipedia.org/wiki/Shunting-yard_algorithm) is executed and produces an abstract syntax tree of `LogicalExpressionNode`, `RelationalExpressionNode` and/or `ValueNode`.

The root of the tree must be either a `LogicalExpressionNode` or a `RelationalExpressionNode` which both implements `Predicate`.
