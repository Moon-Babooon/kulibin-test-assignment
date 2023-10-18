package base;

import lombok.Getter;
import java.util.HashMap;
import java.util.Objects;

public class DataHolder {

    @Getter
    private static final DataHolder instance = new DataHolder();
    private final HashMap<String, Object> holder;

    private DataHolder() {
        this.holder = new HashMap<>();
    }

    public Object get(String key) {
        return holder.get(key);
    }

    public void put(String key, Object value) {
        holder.put(key, value);
    }

    public void reset() {
        holder.clear();
    }

}
