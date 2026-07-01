package me.rainma22.DuetMCP.Tools.InternetTools.search;

import me.rainma22.DuetMCP.Tools.Tool;
import org.json.JSONObject;

/**
 *
 */
public abstract class InternetSearchTool implements Tool {

    protected JSONObject conf;
    
    public InternetSearchTool(JSONObject conf){
        this.conf = conf;
    }
}
