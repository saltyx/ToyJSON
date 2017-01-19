package lexer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PushbackInputStream;
/**
 * Created by shiyan on 2017/1/19.
 */
public class FileUtils {

    private PushbackInputStream stream_;

    public FileUtils() {
    }

    public FileUtils(String path) throws IOException {
        stream_ = new PushbackInputStream(new FileInputStream(new File(path)));
    }

    public void setPath(String path) throws IOException {
        stream_ = new PushbackInputStream(new FileInputStream(new File(path)));
    }

    public int read() throws IOException {
        return stream_.read();
    }

    public void unread(int c) throws IOException {
        stream_.unread(c);
    }
}
