package lexer;

import java.io.IOException;

/**
 * Created by shiyan on 2017/1/21.
 * Contact: shiyan233@hotmail.com
 *          saltyx.github.io
 */
public interface BaseUtils {
    void setSource(String source) throws IOException;
    int read() throws IOException;
    void unread(int at) throws IOException;
}
