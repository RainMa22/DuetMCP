package me.rainma22.DuetMCP.Tools.builtins.fetch;

import java.util.List;
import java.util.Map;
import me.rainma22.DuetMCP.Tools.ToolPlugin;
import me.rainma22.DuetMCP.Utils.ConfigurationManager;
import me.rainma22.DuetMCP.Utils.ResourceRegistries;
import me.rainma22.DuetMCP.Utils.Tuple;

/**
 *
 */
public class InternetFetchToolPlugin implements ToolPlugin {

    private System.Logger logger = System.getLogger(this.getClass().getName());

    @Override
    public void onLoad(ResourceRegistries registries) {
        final Map<String, Object> INFO = Map.of(
                "name", "duet_directInternetFetch",
                "title", "Web Resource Fetching",
                "description", "Fetch an internet resource using HTTP GET; \n"
                + "make sure to URL-encode your parameters.",
                "inputSchema", Map.of(
                        "type", "object",
                        "properties", Map.of(
                                "url", Map.of(
                                        "type", "string",
                                        "description", "URL of the web resource"),
                                "header", Map.of(
                                        "type", "object",
                                        "description", "the header for the GET request"))),
                "required", List.of("url")
        );
        final Map<String, Object> DEFAULT_CONFIG =
                Map.of("allow local ip access", false);
        var conf = ConfigurationManager.ofClass(this.getClass()).getConfig(DEFAULT_CONFIG);
        registries.toolRegistry()
                .register(INFO.get("name").toString(), new Tuple<>(INFO, new DirectFetchTool(conf)));
    }
}