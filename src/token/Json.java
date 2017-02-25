package token;

import lexer.JsonLexException;
import lexer.Lexer;
import lexer.StringUtils;
import parser.JsonParseException;
import parser.Parser;

import java.io.IOException;

/**
 * Created by shiyan on 2017/1/19.
 * Contact: shiyan233@hotmail.com
 *          saltyx.github.io
 */
public class Json extends Object {

    public static <T> T fromJson(String source, Class<T> clss) throws IOException,
            JsonLexException, JsonParseException {
        Parser parser = new Parser(new Lexer(new StringUtils(source)));
        Json json = parser.parse();
        if (json instanceof JsonObject) {

        } else if (json instanceof JsonArray) {
            
        }
        return null;
    }
}
