package me.rainma22.DuetMCP.Tools.builtins.fetch;

import me.rainma22.DuetMCP.Tools.Tool;
import org.json.JSONObject;

/**
 *
 */
public abstract class InternetFetchTool implements Tool {
    protected JSONObject conf;
    public InternetFetchTool(JSONObject conf){
        this.conf = conf;
    }
}
