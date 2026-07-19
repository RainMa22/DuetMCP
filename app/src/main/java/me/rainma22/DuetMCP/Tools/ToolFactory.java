package me.rainma22.DuetMCP.Tools;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import me.rainma22.DuetMCP.Exception.BadRequestException;
import me.rainma22.DuetMCP.UserContext;
import me.rainma22.DuetMCP.Utils.SessionManager;
import me.rainma22.DuetMCP.event.ServerNotification;
import org.json.JSONObject;

/**
 *
 */
public class ToolFactory {
    
    private static final Map<String, ToolEntry> TOOL_MAP = new HashMap<>();
    private static final ServerNotification TOOLCHANGED_NOTIF = 
            new ServerNotification("notifications/tools/listChanged");
    
    public static void registerTool(String name, Map<String, Object> info, Tool t) {
        TOOL_MAP.put(name, new ToolEntry(info, t));
        SessionManager.getInstance().sendEvents(TOOLCHANGED_NOTIF);
    }
    private static final ToolEntry DEFALT_TOOL_ENTRY = new ToolEntry(Map.of(), new Tool() {
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
