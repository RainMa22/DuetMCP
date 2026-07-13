package me.rainma22.DuetMCP.Methods.notifications;

import me.rainma22.DuetMCP.Exception.BadRequestException;
import me.rainma22.DuetMCP.Exception.DuetMCPException;
import me.rainma22.DuetMCP.UserContext;
import org.json.JSONObject;

/**
 *
 */
public class NotificationProcessor implements NotificationVisitor<JSONObject, DuetMCPException>{
    private UserContext uctx; 
    
    public NotificationProcessor(UserContext uctx){
        this.uctx = uctx;
    }
    @Override
    public JSONObject visit(Notification n) throws BadRequestException {
        return null;
    }

    @Override
    public JSONObject visit(NotificationInitialized ni) throws BadRequestException {
        
        return null;
    }

}
