package token;

/**
 * Created by shiyan on 2017/1/19.
 * Contact: shiyan233@hotmail.com
 *          saltyx.github.io
 */
public class Value {

    private int intValue_;
    private double doubleValue_;
    private Object value_;

    private byte flag;

    public Value(int value) {
        this.intValue_ = value;
        flag = 0x00;
    }

    public Value(double value) {
        this.doubleValue_ = value;
        flag = 0x01;
    }

    public Value(Object value) {
        this.value_ = value;
        flag = 0x02;
    }

    public Object get() {
        switch (flag) {
            case 0x00:
                return this.intValue_;
            case 0x01:
                return this.doubleValue_;
            case 0x02:
                return this.value_;
            default:
                return null;
        }
    }

    public JsonArray toJsonArray() {
        return this.value_ == null ? null : (JsonArray) this.value_;
    }

    public JsonObject toJsonObject() {
        return this.value_ == null ? null : (JsonObject) this.value_;
    }

    @Override
    public String toString() {
        switch (flag) {
            case 0x00:
                return String.valueOf(this.intValue_);
            case 0x01:
                return String.valueOf(this.doubleValue_);
            case 0x02:
                return this.value_.toString();
            default:
                return "";
        }
    }
}
