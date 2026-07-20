package me.rainma22.DuetMCP.abstracts;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import me.rainma22.DuetMCP.Utils.SessionManager;
import me.rainma22.DuetMCP.Utils.Tuple;
import me.rainma22.DuetMCP.event.ServerNotification;

/**
 *
 */
public abstract class Registry<T> {

    private final Map<String, Tuple<Map<String, Object>, T>> map = new HashMap();

    protected abstract ServerNotification getUpdateNotif();

    protected abstract T defaultEntry();

    public void register(String name, Tuple<Map<String, Object>, T> item) {
        map.put(name, item);
        SessionManager.getInstance().sendEvents(getUpdateNotif());
    }

    public T fromString(String s) {
        T val = map.getOrDefault(s, null).second();
        return val == null ? defaultEntry() : val;
    }

    public Collection<Map<String, Object>> list() {
        return map.entrySet().stream()
                .map((e) -> e.getValue().first())
                .peek(e -> e.forEach((k, v) -> System.out.printf("%s: %s \n", k, v)))
                .toList();
    }

}
