package me.rainma22.DuetMCP.Tools.builtins.time;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import me.rainma22.DuetMCP.Exception.BadRequestException;
import me.rainma22.DuetMCP.Tools.Tool;
import me.rainma22.DuetMCP.Tools.ToolFactory;
import me.rainma22.DuetMCP.Tools.ToolPlugin;
import me.rainma22.DuetMCP.UserContext;
import org.json.JSONObject;

/**
 *
 */
public class TimeToolPlugin implements ToolPlugin {

    private class TimeTool implements Tool {

        @Override
        public JSONObject apply(UserContext uctx, JSONObject arguments) throws BadRequestException {
            var format = arguments.optString("format", "");
            String res;
            if (format.isEmpty()) {
                res = String.valueOf(System.currentTimeMillis() / 1000);
            } else {
                SimpleDateFormat df = new SimpleDateFormat(format);
                res = df.format(Date.from(Instant.now()));
            }
            return new JSONObject(Map.of(
                    "content", List.of(
                            Map.of("type", "text",
                                    "text", res)
                    )
            ));
        }

    }

    @Override
    public void onLoad() {
        final Map<String, Object> INFO = Map.of(
                "name", "duet_getCurrentTime",
                "title", "get Current time",
                "description", "Get Current Time in the MCP's timezone \n"
                + "pass in format string to get a formatted time.\n"
                + "returns: the current unix time in seconds if format string is empty,\n"
                + "otherwise return a string based on the format string\n"
                + "example: duet_getCurrentTime() -> 1783980846\n"
                + "example: duet_getCurrentTime(format=\"yyyy-MM-dd\") -> 2026-07-13",
                "inputSchema", Map.of(
                        "type", "object",
                        "properties", Map.of(
                                "format", Map.of(
                                        "type", "string",
                                        "description", "formatter string for time.\n"
                                        + "'y' is year;\n"
                                        + "'M' is month;\n"
                                        + "'d' is day;\n"
                                        + "'H' is hour;\n"
                                        + "'m' is minute;\n"
                                        + "'s' is second.\n"))),
                "required", List.of("")
        );
        ToolFactory.registerTool(INFO.get("name").toString(), INFO, new TimeTool());
    }
}
