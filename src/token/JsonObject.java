package token;

import java.util.Map;

/**
 * Created by shiyan on 2017/1/19.
 */
public class JsonObject extends Json {
    private Map<String, Value> pair_;

    public JsonObject(Map<String, Value> pair) {
        this.pair_ = pair;
    }

    public Value get(String key) {
        return this.pair_.get(key);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (String key: this.pair_.keySet()) {
            builder.append(key);
            builder.append(" : ");
            builder.append(this.pair_.get(key).toString());
            builder.append(';');
        }
        return String.format("JsonObject[%s]",builder.toString());
    }
}
