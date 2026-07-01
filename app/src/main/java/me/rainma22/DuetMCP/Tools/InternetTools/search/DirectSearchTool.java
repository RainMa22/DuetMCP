package me.rainma22.DuetMCP.Tools.InternetTools.search;

import java.io.IOException;
import java.net.URI;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import me.rainma22.DuetMCP.Exception.BadRequestException;
import me.rainma22.DuetMCP.UserContext;
import me.rainma22.DuetMCP.Utils.Downloader;
import me.rainma22.DuetMCP.Utils.ExaAPI;
import org.json.JSONObject;

/**
 *
 */
public class DirectSearchTool extends InternetSearchTool {

    private System.Logger logger = System.getLogger(this.getClass().getName());
    private List supportedFormatters = List.of();

    public DirectSearchTool(JSONObject conf) {
        super(conf);
    }

    @Override
    public JSONObject apply(UserContext param1, JSONObject args) throws BadRequestException {
        String baseUrl = conf.getString("url");
        if (baseUrl.isBlank()) {
            throw new BadRequestException("base URL is not set! Please contact MCP host.");
        } else if (!baseUrl.contains("<|query|>")) {
            throw new BadRequestException("base URL does not contain the base query token! Please contact MCP host.");
        }
        String formatterStr = conf.getString("formatter");
        if (formatterStr == null || !supportedFormatters.contains(formatterStr)) {
            String warnFormat = "Unknown formatter \"%s\", defaulting to direct HTML results";
            logger.log(System.Logger.Level.WARNING, String.format(warnFormat,formatterStr));
        }
        if (formatterStr.isEmpty()) {
            logger.log(System.Logger.Level.WARNING, "using direct HTML result of website, which is extremely token-inefficient.");
        }
        try {
            String query = args.optString("query");
            query = query == null ? "" : query;
            URI uri = URI.create(baseUrl.replace("<|query|>", query));
            String res = Downloader.get(uri).toString();

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
