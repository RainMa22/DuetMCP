package me.rainma22.DuetMCP.Tools;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import me.rainma22.DuetMCP.Exception.BadRequestException;
import me.rainma22.DuetMCP.UserContext;
import org.json.JSONObject;

/**
 *
 */
public class ToolFactory {

    private static final Map<String, ToolEntry> TOOL_MAP = new HashMap<>();

    public static void registerTool(String name, Map<String, Object> info, Tool t) {
        TOOL_MAP.put(name, new ToolEntry(info, t));
    }
    private static final ToolEntry DEFALT_TOOL_ENTRY = new ToolEntry(Map.of(), new Tool() {
        @Override
        public void onLoad() {
        }

        @Override
        public JSONObject apply(UserContext param1, JSONObject param2) throws BadRequestException {
            throw new UnsupportedOperationException("Not supported yet."); 
        }
    });

    public static Collection<Map<String, Object>> availableTools() {
        return TOOL_MAP.entrySet().stream()
                .map((mapEntry) -> mapEntry.getValue().info())
                .toList();
    }

    public static Tool fromString(String str) {
        return TOOL_MAP.getOrDefault(str, DEFALT_TOOL_ENTRY).tool();
    }
}

record ToolEntry(Map<String, Object> info, Tool tool){}
