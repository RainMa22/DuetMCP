package me.rainma22.DuetMCP.Tools;

import me.rainma22.DuetMCP.Exception.DuetMCPException;
import me.rainma22.DuetMCP.UserContext;
import me.rainma22.DuetMCP.abstracts.Registry;
import me.rainma22.DuetMCP.event.ServerNotification;
import org.json.JSONObject;

/**
 *
 */
public class ToolRegistry extends Registry<Tool>{
    
    private final ServerNotification TOOLCHANGED_NOTIF = 
            new ServerNotification("notifications/tools/listChanged");

    @Override
    protected ServerNotification getUpdateNotif() {
        return TOOLCHANGED_NOTIF;
    }

    @Override
    protected Tool defaultEntry() {
        return new Tool(){
            @Override
            public JSONObject apply(UserContext param1, JSONObject param2) throws DuetMCPException {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }
      
}

