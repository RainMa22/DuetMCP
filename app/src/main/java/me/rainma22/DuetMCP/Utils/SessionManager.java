package me.rainma22.DuetMCP.Utils;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.WeakHashMap;
import me.rainma22.DuetMCP.UserContext;

/**
 *
 */
public class SessionManager {
    
    private static SessionManager manager = null;
    
    public static void setNumMappings(int nMappings) {
        if (manager != null) {
            var oldMap = manager.sessionMap;
            manager.sessionMap = new WeakHashMap<>(nMappings);
            manager.sessionMap.putAll(oldMap);
        }
    }
    
    public static SessionManager getInstance() {
        if (manager == null) {
            manager = new SessionManager();
        }
        return manager;
    }

    private Map<UUID, UserContext> sessionMap = new WeakHashMap<>(512);
    
    public UUID newSession(UserContext ctx) {
        var id = UUID.randomUUID();
        ctx.sessionId = new WeakReference<>(id);
        sessionMap.put(id, ctx);
        return id;
    }
    
    public Optional<UserContext> getContextOf(String id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(sessionMap.getOrDefault(
                UUID.fromString(id), null));
    }
    
}
