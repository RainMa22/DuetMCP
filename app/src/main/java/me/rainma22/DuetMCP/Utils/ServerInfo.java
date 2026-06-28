package me.rainma22.DuetMCP.Utils;

import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 */
public class ServerInfo {

    public static final JSONObject CAPABILITY_JSON
            = new JSONObject(Map.of(
                    "tools", Map.of("listChanged", true)
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
                    "version", "0.0.1",
                    "description", "A WIP MCP server made in Java",
                    "icons", new JSONArray(),
                    "websiteUrl", ""
            ));
}
