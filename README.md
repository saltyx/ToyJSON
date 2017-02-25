#一个简单的JSON解析器

## 概要
- Lexer根据DFA构造
- Parser根据LL(1)文法递归下降程序构造

**[详细实现请查看博客，点击这里](https://saltyx.github.io/2017/02/21/JSON-Parser/)**

## 项目结构
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
