package me.rainma22.DuetMCP.prompts;

import me.rainma22.DuetMCP.Exception.DuetMCPException;
import me.rainma22.DuetMCP.UserContext;
import me.rainma22.DuetMCP.abstracts.Registry;
import me.rainma22.DuetMCP.event.ServerNotification;
import org.json.JSONObject;

/**
 *
 */
public class PromptRegistry extends Registry<Prompt> {

    @Override
    protected ServerNotification getUpdateNotif() {
        return new ServerNotification("notifications/prompts/listChanged");
    }

    @Override
    protected Prompt defaultEntry() {
        return new Prompt() {
            @Override
            public JSONObject apply(UserContext param1, JSONObject param2) throws DuetMCPException {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }
}
