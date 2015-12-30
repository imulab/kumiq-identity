## Tokenizer

Tokenizer is used to break down `SCIM path` and/or `SCIM filter` down to lexical units so it can be parsed and processed by the compilers. The two implementations are `PathTokenizer` and `FilterTokenizer`.

It is a stateful object which defines the following properties:
- `charSequence`: the sequence of characters to be broken down.
- `delimiters`: a list of delimiters to break down words, defaulted to `.`
- `keywordTokens`: a list of keywords that is itself a token. A keyword ignores the normal delimiter rule. For example, left and right brackets are itself tokens without leading and trailing space; SCIM URNs are itself a token, despite it might contain periods.
