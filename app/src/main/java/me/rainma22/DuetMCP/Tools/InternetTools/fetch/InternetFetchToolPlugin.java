package me.rainma22.DuetMCP.Tools.InternetTools.fetch;

import java.util.List;
import java.util.Map;
import me.rainma22.DuetMCP.Tools.ToolFactory;
import me.rainma22.DuetMCP.Tools.ToolPlugin;
import me.rainma22.DuetMCP.Utils.ConfigurationManager;

/**
 *
 */
public class InternetFetchToolPlugin implements ToolPlugin {

    private System.Logger logger = System.getLogger(this.getClass().getName());

    @Override
    public void onLoad() {
        final Map<String, Object> INFO = Map.of(
                "name", "duet_InternetFetch",
                "title", "Web Resource Fetching",
                "description", "Fetch an internet resource using HTTP GET; \n"
                + "Remember to prefix the url with http or https.\n"
                + "When adding parameters to the url, "
                + "make sure to URL-encode your parameters.",
                "inputSchema", Map.of(
                        "type", "object",
                        "properties", Map.of("url", Map.of("type", "string",
                                "description", "URL of the web resource"))),
                 "required", List.of("url")
        );
//        final Map<String, Object> apiDefault = Map.of(
//                "vendor", ExaAPI.VENDOR_NAME,
//                "API_KEY", ""
//        );
        final Map<String, Object> downloadDefault = Map.of(
                "type", "direct download"
        );
        final Map<String, Object> defaultConfig = Map.of(
                "type", "direct download",
                //                "api_setting", apiDefault,
                "download_setting", downloadDefault
        );
        var conf = ConfigurationManager.ofClass(this.getClass()).getConfig(defaultConfig);
        switch (conf.getString("type")) {
//            case "api":
//                ToolFactory.registerTool(INFO.get("name").toString(), INFO, new ApiFetchTool(conf.getJSONObject("api_setting")));
//                break;
            default:
                logger.log(System.Logger.Level.WARNING, String.format("Unsupported type %s", conf.getString("type")));
                logger.log(System.Logger.Level.WARNING, "defaulting to direct download...");
            //fallthrough
            case "direct download":
                var downloadConf = conf.getJSONObject("download_setting");
                ToolFactory.registerTool(INFO.get("name").toString(), INFO, new DirectFetchTool(downloadConf));
                break;
        }
    }

}
