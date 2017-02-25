package token;

/**
 * Created by shiyan on 2017/1/19.
 * Contact: shiyan233@hotmail.com
 *          saltyx.github.io
 */
public class Token {
    private JsonToken token_;
    private Value value_;

    public Token(JsonToken token, Value value) {
        this.token_ = token;
        this.value_ = value;
    }

    public JsonToken getToken() {
        return this.token_;
    }

    public Value getValue() {
        return this.value_;
    }

    @Override
    public String toString() {
        return "Token[token_=" + this.token_ + ", value_=" + this.value_ + ']';
    }
}
