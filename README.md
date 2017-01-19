#一个简单的JSON解析器
- Lexer根据DFA构造
- Parser根据LL(1)文法递归下降程序构造
```
. 
├── lexer 
│   ├── FileUtils.java
│   ├── JsonLexException.java
│   └── Lexer.java
├── parser
│   ├── JsonParseException.java
│   └── Parser.java
├── test
│   └── Test.java
└── token
    ├── JsonArray.java
    ├── Json.java
    ├── JsonObject.java
    ├── JsonToken.java
    ├── Token.java
    └── Value.java
```
