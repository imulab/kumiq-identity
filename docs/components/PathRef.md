## Path Reference

A `PathRef` is a reference to a `PathToken`. The `PathCompiler` produces internally produces a tree of `PathToken`. For example:

```
emails[primary eq false].value may evaluate to:
                   Root
                  /    \
           emails[0]  emails[2]
              |         |
            value      value

Each node is PathToken
```

The `PathCompiler` externally would produce a one or more linked-lists of all the traversable paths of that tree, represented by the head of each linked-list. Each node is `PathRef`, wrapping a `PathToken` inside and delegate most of the work to the wrapped token.

In other words, from the above example, it would produce:

```
List1: Root->emails[0]->value
List2: Root->emails[2]->value

Each node is PathRef
```

The introduction of `PathRef` prevents complex split and re-creation of tree branches to linked-lists while providing reliable reference to each traversable path inferred by the SCIM path/filter.
