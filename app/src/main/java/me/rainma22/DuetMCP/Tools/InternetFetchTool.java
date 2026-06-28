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
public class InternetFetchTool extends Tool {

    @Override
    public void onLoad() {
        final Map<String, Object> INFO = Map.of(
                "name", "internetFetch",
                "title", "Web Resource Fetching",
                "description", "fetch an internet resource",
                "inputSchema", Map.of(
                        "type", "object",
                        "properties", Map.of("url", Map.of("type", "string",
                                "description", "URL of the web resource"))),
                "required", List.of("url"));
        ToolFactory.registerTool(INFO.get("name").toString(), INFO, new InternetFetchTool());
    }

    
    @Override
    public JSONObject apply(UserContext uctx, JSONObject arguments) throws BadRequestException {
        try {
            String url = arguments.optString("url");
            String res = DownloadUtils.get(URI.create(url));
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
