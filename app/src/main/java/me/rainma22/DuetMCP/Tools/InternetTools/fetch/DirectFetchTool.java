package me.rainma22.DuetMCP.Tools.InternetTools.fetch;

import java.io.IOException;
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

        public DirectFetchTool(JSONObject conf) {
            super(conf);
        }

        public JSONObject apply(UserContext uctx, JSONObject arguments) throws BadRequestException {
            try {
                String url = arguments.optString("url");
                String res = Downloader.get(URI.create(url));
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