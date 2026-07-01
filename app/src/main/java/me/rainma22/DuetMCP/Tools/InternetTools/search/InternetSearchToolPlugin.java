package me.rainma22.DuetMCP.Tools.InternetTools.search;

import java.util.List;
import java.util.Map;
import me.rainma22.DuetMCP.Tools.ToolFactory;
import me.rainma22.DuetMCP.Tools.ToolPlugin;
import me.rainma22.DuetMCP.Utils.ConfigurationManager;
import me.rainma22.DuetMCP.Utils.ExaAPI;
import org.json.JSONObject;

/**
 *
 */
public class InternetSearchToolPlugin implements ToolPlugin {

    private System.Logger logger = System.getLogger(this.getClass().getName());
    protected JSONObject conf;

    @Override
    public void onLoad() {
        final Map<String, Object> INFO = Map.of(
                "name", "internetSearch",
                "title", "Web Resource Searching",
                "description", "Searches the internet about a query",
                "inputSchema", Map.of(
                        "type", "object",
                        "properties", Map.of("query", Map.of("type", "string",
                                "description", "query in natural text"))),
                "required", List.of("query"));
        final Map<String, Object> apiDefault = Map.of(
                "vendor", ExaAPI.VENDOR_NAME,
                "API_KEY", ""
        );
        final Map<String, Object> naiveDefault = Map.of(
                "method", "naive",
                "url", "",
                "formatter", ""
        );
        final Map<String, Object> defaultConfig = Map.of(
                "type", "direct download",
                "api_setting", apiDefault,
                "download_setting", naiveDefault
        );
        conf = ConfigurationManager.ofClass(this.getClass()).getConfig(defaultConfig);
        switch (conf.getString("type")) {
            case "api":
                ToolFactory.registerTool(INFO.get("name").toString(), INFO, new ApiSearchTool(conf.getJSONObject("api_setting")));
                break;
            default:
                logger.log(System.Logger.Level.WARNING, "defaulting to direct download...");
            //fallthrough
            case "direct download":
                var downloadConf = conf.getJSONObject("download_setting");
                ToolFactory.registerTool(INFO.get("name").toString(), INFO, new DirectSearchTool(downloadConf));
                break;

        }

    }
}
