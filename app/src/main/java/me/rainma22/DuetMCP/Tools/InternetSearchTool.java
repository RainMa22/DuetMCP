package me.rainma22.DuetMCP.Tools;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import me.rainma22.DuetMCP.Exception.BadRequestException;
import me.rainma22.DuetMCP.UserContext;
import me.rainma22.DuetMCP.Utils.DownloadUtils;
import org.json.JSONObject;

/**
 *
 */
public class InternetSearchTool extends Tool {

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
        ToolFactory.registerTool(INFO.get("name").toString(), INFO, new InternetSearchTool());
    }

    @Override
    public JSONObject apply(UserContext uctx, JSONObject arguments) throws BadRequestException {
        String query = arguments.optString("query", "");
        String url = "https://www.duckduckgo.com/";
        try {
            String res = DownloadUtils.get(URI.create(url), Map.of("q", query));
            return new JSONObject(Map.of(
                    "content", List.of(
                            Map.of("type", "text",
                                    "text", res)
                    )
            ));
        } catch (IOException | InterruptedException ex) {
            throw new BadRequestException(ex);
        }
    }
}
