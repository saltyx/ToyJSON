package lexer;

import java.io.IOException;

/**
 * Created by shiyan on 2017/1/21.
 * Contact: shiyan233@hotmail.com
 *          saltyx.github.io
 */
public class StringUtils implements BaseUtils {
    private int index_;
    private String string_;

    public StringUtils() {
    }

    public StringUtils(String string) {
        this.string_ = string;
        this.index_ = 0;
    }

    @Override
    public void setSource(String source) throws IOException {
        this.string_ = source;
        this.index_ = 0;
    }

    @Override
    public int read() throws IOException {
        return this.string_.length() <= this.index_ ? -1 : this.string_.charAt(this.index_++);
    }

    @Override
    public void unread(int at) throws IOException {
        --this.index_;
    }
}
