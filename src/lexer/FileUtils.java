package lexer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PushbackInputStream;
/**
 * Created by shiyan on 2017/1/19.
 * Contact: shiyan233@hotmail.com
 *          saltyx.github.io
 */
public class FileUtils implements BaseUtils {

    private PushbackInputStream stream_;

    public FileUtils() {

    }

    public FileUtils(String source) throws IOException {
        stream_ = new PushbackInputStream(new FileInputStream(new File(source)));
    }

    @Override
    public void setSource(String source) throws IOException {
        stream_ = new PushbackInputStream(new FileInputStream(new File(source)));
    }

    @Override
    public int read() throws IOException {
        return stream_.read();
    }

    @Override
    public void unread(int c) throws IOException {
        stream_.unread(c);
    }
}
