package parser;

import lexer.JsonLexException;
import lexer.Lexer;
import token.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by shiyan on 2017/1/19.
 */
public class Parser {
    private Lexer lexer_;

    public Parser(Lexer lexer) {
        this.lexer_ = lexer;
    }

    public Parser() {
    }

    public void setLexer(Lexer lexer) {
        this.lexer_ = lexer;
    }

    public Json parse() throws IOException, JsonParseException, JsonLexException {
        Token token = lexer_.scan();
        if (token.getToken() == JsonToken.BEGIN_OBJ) {
            return object();
        } else if (token.getToken() == JsonToken.BEGIN_ARR) {
            return array();
        } else {
            error("parse", token.toString());
            return null;
        }
    }

    //文法 { member }
    public JsonObject object() throws IOException,
            JsonParseException, JsonLexException {
        Map<String, Value> map = new HashMap<>();
        return new JsonObject(member(map));
    }

    //对应文法 pair mem' | eps
    public Map<String ,Value> member(Map<String, Value> map) throws IOException,
            JsonLexException, JsonParseException {
        Token token = lexer_.scan();
        if (token.getToken() == JsonToken.END_OBJ) { //eps
            return map;
        }
        return member_1( pair(map) );
    }

    //对应文法 , pair mem' | eps
    public Map<String, Value> member_1(Map<String, Value> map) throws IOException, JsonLexException, JsonParseException {
        Token token = lexer_.scan();
        if (token.getToken() == JsonToken.COMMA) { //,
            lexer_.scan();
            return member_1( pair(map) );
        } else if (token.getToken() == JsonToken.END_OBJ) { //eps
            return map;
        } else {
            error("member_1", token.toString());
            return null;
        }
    }

    //文法 string : value
    private Map<String, Value> pair(Map<String, Value> map) throws IOException, JsonLexException, JsonParseException {
        Token token = lexer_.peek();
        if (token.getToken() == JsonToken.STRING) {
            String key = (String) token.getValue().get();
            token = lexer_.scan();
            if (token.getToken() == JsonToken.COLON) {
                lexer_.scan();
                map.put(key, value());
                return map;
            } else {
                error("pair", token.toString());
                return null;
            }
        } else {
            error("pair", token.toString());
            return null;
        }
    }

    //文法 [ elem ]
    public JsonArray array() throws IOException,JsonParseException, JsonLexException  {
        List<Value> list = new LinkedList<>();
        return new JsonArray(elem(list));
    }
    //文法 value elem' | eps
    public List<Value> elem(List<Value> list) throws IOException,
            JsonLexException,JsonParseException  {
        Token token = lexer_.scan();
        if (token.getToken() == JsonToken.END_ARR) { // eps
            return list;
        } else {
            list.add(value());
            return elem_1(list);
        }
    }

    // 文法 , value elem' | eps
    public List<Value> elem_1(List<Value> list) throws IOException,
            JsonLexException, JsonParseException  {
        Token token = lexer_.scan();
        if (token.getToken() == JsonToken.COMMA) { // ,
            lexer_.scan();
            list.add(value());
            return elem_1(list);
        } else if (token.getToken() == JsonToken.END_ARR) { // eps
            return list;
        } else {
            error("elem_1",token.toString());
            return null;
        }
    }

    public Value value() throws IOException, JsonLexException,
            JsonParseException {
        Token token = lexer_.peek();
        if (isCommonValue(token.getToken())) {
            return new Value(token.getValue());
        } else if (token.getToken() == JsonToken.BEGIN_OBJ) {
            return new Value(object());
        } else if (token.getToken() == JsonToken.BEGIN_ARR) {
            return new Value(array());
        } else {
            error("value",token.toString());
            return null;
        }
    }

    private boolean isCommonValue(JsonToken token) {
        return (token == JsonToken.STRING || token == JsonToken.FALSE ||
                token == JsonToken.INT || token == JsonToken.DOUBLE ||
                token == JsonToken.TRUE || token == JsonToken.NULL) ? true : false;
    }


    public void error(String position, String value) throws JsonParseException {
        throw new JsonParseException(String.format("an error occurred on Parser [%s %s]",position,value));
    }
}
