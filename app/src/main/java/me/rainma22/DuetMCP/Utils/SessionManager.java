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
    
    private Map<UUID, UserContext> SessionMap = new WeakHashMap<>();
    public UUID newSession(UserContext ctx){
        var id = UUID.randomUUID();
        ctx.sessionId = id;
        SessionMap.put(id, ctx);
        return id;
    }
    
    public Optional<UserContext> getContextOf(UUID id){
        if(id == null) return Optional.empty();
        return Optional.ofNullable(SessionMap.get(id));
    }
    
}
