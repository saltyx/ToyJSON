package token;

import java.util.List;

/**
 * Created by shiyan on 2017/1/19.
 */
public class JsonArray extends Json {
    private List<Value> list_;

    public JsonArray(List<Value> list) {
        this.list_ = list;
    }

    public void add(Value value) {
        this.list_.add(value);
    }

    public int size() {
        return this.list_.size();
    }

    public Value get(int index) {
        return this.list_.get(index);
    }

    @Override
    public String toString() {
        return String.format("JsonArray:[%s]",
                this.list_.stream().map((x) -> x.toString()+";").reduce((x, y) -> x+y).get());
    }
}
