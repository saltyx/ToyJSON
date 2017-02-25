package lexer;

import token.JsonToken;
import token.Token;
import token.Value;

import java.io.IOException;

/**
 * Created by shiyan on 2017/1/19.
 * Contact: shiyan233@hotmail.com
 *          saltyx.github.io
 */
public class Lexer {
    public enum READ_MODE {
        READ_FROM_FILE,READ_FROM_STRING
    }

    private BaseUtils in_;
    private int line_;
    private int at_;
    private Token token_;

    public Lexer(BaseUtils in) {
        this.in_ = in;
        init();
    }

    public Lexer() {
    }

    public void init() {
        this.at_ = '\0';
        this.line_ = 0;
        this.token_ = null;
    }
    //修改文件路径，重新进行分析
    public void resetSource(String source, READ_MODE mode) throws IOException {
        if (mode == READ_MODE.READ_FROM_FILE
                && (this.in_ == null || this.in_ instanceof StringUtils)) {
            this.in_ = new FileUtils();
        } else if (mode == READ_MODE.READ_FROM_STRING
                && (this.in_ == null || this.in_ instanceof FileUtils)){
            this.in_ = new StringUtils();
        }

        this.in_.setSource(source);
        init();
    }

    public Token peek() {
        return this.token_;
    }

    public Token scan() throws IOException, JsonLexException {
        //this.at_ = '\0';
        while (isWhiteBlack( this.at_ = read() ));
        switch ((int)this.at_) {
            case '{':
                return this.token_ = new Token(JsonToken.BEGIN_OBJ, new Value("{"));
            case '}':
                return this.token_ = new Token(JsonToken.END_OBJ, new Value("}"));
            case '[':
                return this.token_ = new Token(JsonToken.BEGIN_ARR, new Value("["));
            case ']':
                return this.token_ = new Token(JsonToken.END_ARR, new Value("]"));
            case ':':
                return this.token_ = new Token(JsonToken.COLON, new Value(":"));
            case ',':
                return this.token_ = new Token(JsonToken.COMMA, new Value(","));
            case '1': case '2': case '3': case '4': case '5':
            case '6': case '7': case '8': case '9': case '0':
            case '-': case '+': case '.':
                return this.token_ = readNumber(this.at_);
            case '\"':
                return this.token_ = readString();
            case 'n':
                return this.token_ = readNull();
            case 't':
                return this.token_ = readTrue();
            case 'f':
                return this.token_ = readFalse();
            case -1:
                return this.token_ = new Token(JsonToken.EOF, new Value("eof"));
            default:
                this.token_ = null;
                error("scan->default",this.at_);
                return null;
        }
    }

    private Token readString() throws IOException, JsonLexException {
        StringBuilder builder = new StringBuilder();
        while ( (this.at_ = read()) != '\"') {
            if (this.at_ == '\r' || this.at_ == '\n') error("string", this.at_);
            if (this.at_ == '\\') {
                //check escape char
                this.at_ = read();
                if (isEscapeChar(this.at_)) {
                    builder.append('\\');
                    builder.append((char) this.at_);
                } else if (this.at_ == 'u'){
                    //unicode
                    builder.append('\\');
                    builder.append('u');
                    builder.append(unicode());

                } else {
                    error("string", this.at_);
                }
            } else {
                builder.append((char) this.at_);
            }
        }
        if ( this.at_ != '\"') {
            //string is not closed
            error("string[not closed]", this.at_);
        }
        return this.token_ = new Token(JsonToken.STRING, new Value(builder.toString()));
    }

    private String unicode() throws IOException, JsonLexException {
        StringBuilder builder = new StringBuilder();
        int i=0;
        for (;i<4 && isHexChar(this.at_ = read()); builder.append((char) this.at_),++i);
        if (i < 4) error("unicode", this.at_);
        return builder.toString();
    }


    private Token readFalse() throws IOException, JsonLexException {
        char []temp = {'a', 'l', 's', 'e'};
        for (int i=0; i<4; ++i) {
            this.at_ = read();
            if (this.at_ != temp[i]) {
                error("false", this.at_);
            }
        }
        return this.token_ = new Token(JsonToken.FALSE, new Value("false"));
    }

    private Token readTrue() throws IOException, JsonLexException {
        char []temp = {'r', 'u', 'e'};
        for (int i=0; i<3; ++i) {
            this.at_ = read();
            if (this.at_ != temp[i]) {
                error("true", this.at_);
            }
        }
        return this.token_ = new Token(JsonToken.TRUE, new Value("true"));
    }

    private Token readNull() throws IOException, JsonLexException {
        char [] temp = {'u', 'l', 'l'};
        for (int i=0; i<3; ++i) {
            this.at_ = read();
            if (this.at_ != temp[i]) {
               error("null", this.at_);
            }
        }
        return this.token_ = new Token(JsonToken.NULL, new Value("null"));
    }

    private Token readNumber(int at) throws IOException,JsonLexException {

        StringBuilder builder = new StringBuilder();
        int status = 0;
        while (true) {
            switch (status) {
                case 0:
                    this.at_ = at;
                    if (this.at_ == '+' || this.at_ == '-') status = 1;
                    else if (Character.isDigit(this.at_)) status = 2;
                    else error("number", this.at_);
                    builder.append((char) this.at_);
                    break;
                case 1:
                    this.at_ = read();
                    if (Character.isDigit(this.at_)) status = 2;
                    else error("number", this.at_);
                    builder.append((char) this.at_);
                    break;
                case 2:
                    this.at_ = read();
                    if (this.at_ == '.') status = 3;
                    else if (Character.isDigit(this.at_)) status = 2;
                    else if (this.at_ == 'E' || this.at_ == 'e') status = 5;
                    else status = 8;
                    if (status != 8) {
                        builder.append((char) this.at_);
                    }
                    break;
                case 3:
                    this.at_ = read();
                    if (Character.isDigit(this.at_)) status = 4;
                    else error("number", this.at_);
                    builder.append((char) this.at_);
                    break;
                case 4:
                    this.at_ = read();
                    if (Character.isDigit(this.at_)) status = 4;
                    else if (this.at_ == 'E' || this.at_ == 'e') status = 5;
                    else status = 9;
                    if (status != 9) {
                        builder.append((char) this.at_);
                    }
                    break;
                case 5:
                    this.at_ = read();
                    if (this.at_ == '+' || this.at_ == '-') status = 6;
                    else if (Character.isDigit(this.at_)) status = 7;
                    else error("number", this.at_);
                    builder.append((char) this.at_);
                    break;
                case 6:
                    this.at_ = read();
                    if (Character.isDigit(this.at_)) status = 7;
                    else error("number", this.at_);
                    builder.append((char) this.at_);
                    break;
                case 7:
                    this.at_ = read();
                    if (Character.isDigit(this.at_)) status = 7;
                    else status = 10;
                    if (status != 10) {
                        builder.append((char) this.at_);
                    }
                    break;
                case 8: // int
                    unread(this.at_); // not a digit
                    return this.token_ = new Token(JsonToken.INT, new Value(Integer.valueOf(builder.toString())));
                case 9: //double without 'E' or 'e'
                    unread(this.at_); // not a digit
                    return this.token_ = new Token(JsonToken.DOUBLE, new Value(Double.valueOf(builder.toString())));
                case 10://double with 'E' or 'e'
                    unread(this.at_);// not a digit
                    return this.token_ = new Token(JsonToken.DOUBLE,new Value(Double.valueOf(builder.toString())));
                default:
                    error("number", this.at_);
            }
        }
    }

    private boolean isEscapeChar(int at) {
        return (at == '"' || at == '\\' || at == '/' || at == 'r'
                || at == 'n' || at == 'b' || at == '\'' || at == 't'
                || at == 'v' || at == 'f') ? true : false;
    }

    private boolean isHexChar(int at) {
        return (Character.isDigit(at) || (at >= 'a' && at <= 'f')
                || (at >= 'A' && at <= 'F')) ? true : false;
    }


    private boolean isWhiteBlack(int at) {
        if (at == '\n' || at == '\r') {
            this.line_ ++;
        }
        return (at == '\t' || at == ' ' || at == '\n' || at == '\r') ? true : false;
    }

    private void error(String errorPosition, int value) throws JsonLexException {
        throw new JsonLexException(String.format("an error occurred on Lexer [%s '%c']", errorPosition, (char)value));
    }

    private int read() throws IOException {
        return this.in_.read();
    }

    private void unread(int c) throws IOException {
        this.in_.unread(c);
    }
}
