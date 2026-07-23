package me.rainma22.DuetMCP.resources.templates;

import me.rainma22.DuetMCP.abstracts.Registry;
import me.rainma22.DuetMCP.event.ServerNotification;

/**
 *
 */
public class ResourceTemplateRegistry extends Registry<ResourceTemplate> {

    @Override
    protected ServerNotification getUpdateNotif() {
        return new ServerNotification("notifications/resources/listChanged");
    }

    @Override
    protected ResourceTemplate defaultEntry() {
        return new ResourceTemplate() {
            @Override
            public JSONObject apply(UserContext param1, JSONObject param2) throws DuetMCPException {
                throw new DuetMCPException("Unknown resource template"); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }
        };
    }
}
