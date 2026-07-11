package me.rainma22.DuetMCP.Utils;

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
    public static SessionManager getInstance(){
        if(manager == null) manager = new SessionManager();
        return manager;
    };
    
    private Map<String, UserContext> SessionMap = WeakHashMap.newWeakHashMap(512);
    public UUID newSession(UserContext ctx){
        var id = UUID.randomUUID();
        ctx.sessionId = id;
        SessionMap.put(id.toString(), ctx);
        return id;
    }
    
    public Optional<UserContext> getContextOf(String id){
        if(id == null) return Optional.empty();
        return Optional.ofNullable(SessionMap.getOrDefault(id, null));
    }
    
}
