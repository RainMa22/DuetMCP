package me.rainma22.DuetMCP.Tools.InternetTools.fetch;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.util.List;
import java.util.Map;
import me.rainma22.DuetMCP.Exception.BadRequestException;
import me.rainma22.DuetMCP.UserContext;
import me.rainma22.DuetMCP.Utils.Downloader;
import org.json.JSONObject;

/**
 *
 */
public class DirectFetchTool extends InternetFetchTool {

    private final System.Logger LOGGER = System.getLogger(this.getClass().getName());

    public DirectFetchTool(JSONObject conf) {
        super(conf);
    }

    public JSONObject apply(UserContext uctx, JSONObject arguments) throws BadRequestException {
        try {
            String url = arguments.optString("url");
            LOGGER.log(System.Logger.Level.INFO, "received url: " + url);
            if (url == null || url.isEmpty()) {
                throw new BadRequestException("URL cannot be null, undefined or empty.");
            }
            if (!url.toLowerCase().startsWith("http://") && !url.toLowerCase().startsWith("https://")) {
                LOGGER.log(System.Logger.Level.INFO, "received padding url with \"http://\"");
                url = "http://" + url;
            }
            URI uri = URI.create(url);
            if (conf.optBoolean("allow local ip access", false) == false) {
                InetAddress addr = InetAddress.getByName(uri.getHost());
                if (addr.isSiteLocalAddress()) {
                    LOGGER.log(System.Logger.Level.INFO, "blocked GET request to address: " + addr.toString());

                    throw new BadRequestException("Local address access not allowed by the MCP host.\n"
                            + "If you think this is a mistake, please contact the MCP host");
                }
            }
            String res = Downloader.get(uri);
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
