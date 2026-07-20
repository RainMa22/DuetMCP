package me.rainma22.DuetMCP.Tools.builtins.time;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import me.rainma22.DuetMCP.Exception.BadRequestException;
import me.rainma22.DuetMCP.Tools.Tool;
import me.rainma22.DuetMCP.Tools.ToolPlugin;
import me.rainma22.DuetMCP.UserContext;
import me.rainma22.DuetMCP.Utils.ResourceRegistries;
import me.rainma22.DuetMCP.Utils.Tuple;
import org.json.JSONObject;

/*
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
    public void onLoad(ResourceRegistries registries) {
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
                                        "description", "formatter string for time (Identitical to Java's SimpleDateFormat).\n"
                                        + "| Letter | Date or Time Component | Presentation | Examples |\n"
                                        + "|-|-|-|-|\n"
                                        + "| G | Era designator | Text | AD |\n"
                                        + "| y | Year | Year | 1996; 96 |\n"
                                        + "| Y | Week year | Year | 2009; 09 |\n"
                                        + "| M | Month in year (context sensitive) | Month | July; Jul; 07 |\n"
                                        + "| L | Month in year (standalone form) | Month | July; Jul; 07 |\n"
                                        + "| w | Week in year | Number | 27 |\n"
                                        + "| W | Week in month | Number | 2 |\n"
                                        + "| D | Day in year | Number | 189 |\n"
                                        + "| d | Day in month | Number | 10 |\n"
                                        + "| F | Day of week in month | Number | 2 |\n"
                                        + "| E | Day name in week | Text | Tuesday; Tue |\n"
                                        + "| u | Day number of week (1 = Monday, ..., 7 = Sunday) | Number | 1 |\n"
                                        + "| a | Am/pm marker | Text | PM |\n"
                                        + "| H | Hour in day (0-23) | Number | 0 |\n"
                                        + "| k | Hour in day (1-24) | Number | 24 |\n"
                                        + "| K | Hour in am/pm (0-11) | Number | 0 |\n"
                                        + "| h | Hour in am/pm (1-12) | Number | 12 |\n"
                                        + "| m | Minute in hour | Number | 30 |\n"
                                        + "| s | Second in minute | Number | 55 |\n"
                                        + "| S | Millisecond | Number | 978 |\n"
                                        + "| z | Time zone | General time zone | Pacific Standard Time; PST; GMT-08:00 |\n"
                                        + "| Z | Time zone | RFC 822 time zone | -0800 |\n"
                                        + "| X | Time zone | ISO 8601 time zone | -08; -0800; -08:00 |"))),
                "required", List.of("")
        );
        registries.toolRegistry()
                .register(INFO.get("name").toString(), 
                        new Tuple<>(INFO, new TimeTool()));
    }
}
