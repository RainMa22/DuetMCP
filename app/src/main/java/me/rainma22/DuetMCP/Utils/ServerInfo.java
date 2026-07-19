package me.rainma22.DuetMCP.Utils;

import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 */
public class ServerInfo {
    public static final List<String> SUPPORTED_VERSIONS = List.of(
            "2025-03-26",
            "2025-06-18",
            "2025-11-25",
            "2026-07-28");
    public static final JSONObject CAPABILITY_JSON
            = new JSONObject(Map.of(
                    "tools", Map.of("listChanged", true)
            //        "prompts", Map.of("listChanged", true)
            //                    //we don't really support tasks yet.
            //                    ,"tasks", Map.of(
            //                            "requests", Map.of(
            //                                    "tools", Map.of(
            //                                            "call", new JSONObject())))
            ));
    public static final JSONObject SERVER_INFO_JSON
            = new JSONObject(Map.of(
                    "name", "DuetMCP",
                    "title", "An MCP server from the ground up",
                    "version", "0.0.2",
                    "description", "A WIP MCP server made in Java",
                    "icons", new JSONArray(),
                    "websiteUrl", ""
            ));
}
