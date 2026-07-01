package me.rainma22.DuetMCP.Tools.InternetTools.search;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import me.rainma22.DuetMCP.Exception.BadRequestException;
import me.rainma22.DuetMCP.UserContext;
import me.rainma22.DuetMCP.Utils.ExaAPI;
import me.rainma22.DuetMCP.Utils.Downloader;
import org.json.JSONObject;

public class ApiSearchTool extends InternetSearchTool {

    public ApiSearchTool(JSONObject conf) {
        super(conf);
    }

    @Override
    public JSONObject apply(UserContext uctx, JSONObject arguments) throws BadRequestException {
        if (!conf.getString("vendor").equals(ExaAPI.VENDOR_NAME)) {
            String str = "Unsuppported API vendor: %s. " + 
                    "please contact the MCP host.";
            throw new BadRequestException(String.format(str, conf.getString("vendor")));
        }
        String query = arguments.optString("query", "");
        String apiKey = super.conf.getString("API_KEY");
        if (apiKey.isEmpty()) {
            throw new BadRequestException("the MCP server host forgot to set the API key, "
                    + "please contact the MCP host.");
        }
        try {
            String URL = ExaAPI.URL;
            URI uri = URI.create(URL).resolve("search");
            String res = Downloader.post(uri,
                    Map.of("content-type", "application/json",
                            "x-api-key", apiKey),
                    new JSONObject(Map.of(
                            "query", query,
                            "contents", Map.of("hightlights", true),
                            "numResults", 10,
                            "type", "auto"
                    )).toString()
            );

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
