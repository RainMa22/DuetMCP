package me.rainma22.DuetMCP.resources;

import me.rainma22.DuetMCP.Exception.DuetMCPException;
import me.rainma22.DuetMCP.UserContext;
import me.rainma22.DuetMCP.abstracts.Registry;
import me.rainma22.DuetMCP.event.ServerNotification;
import org.json.JSONObject;

/**
 *
 */
public class ResourceRegistry extends Registry<Resource> {

    @Override
    protected ServerNotification getUpdateNotif() {
        return new ServerNotification("notifications/resources/listChanged");
    }

    @Override
    protected Resource defaultEntry() {
        return new Resource() {
            @Override
            public JSONObject apply(UserContext param1, JSONObject param2) throws DuetMCPException {
                throw new DuetMCPException("Non-existent Resource.");
            }
        };
    }
}
