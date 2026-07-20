package me.rainma22.DuetMCP.Methods;

import me.rainma22.DuetMCP.Methods.notifications.NotificationInitialized;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.json.JSONObject;

/**
 * Not really a factory pattern
 */
public class MethodFactory {

    private static final Map<String, Function<JSONObject, Method>> METHODS_MAP = new HashMap();

    static {
        registerMethod("initialize", (params) -> new InitializeMethod(params));
        registerMethod("notifications/initialized", (params) -> new NotificationInitialized(params));
        registerMethod("ping", (params)-> new PingMethod(params));
        registerMethod("prompts/list", params -> new PromptListMethod(params));
        registerMethod("prompts/get", params -> new PromptGetMethod(params));
        registerMethod("tools/call", params -> new ToolsCallMethod(params));
        registerMethod("tools/list", param -> new ToolsListMethod(param));
    }
    private static final Function<JSONObject, Method> DEFAULT_METHOD = (param) -> new Method(param) {
        @Override
        public <T, E extends Exception> T accept(MethodVisitor<T, E> visitor) throws E {
            return visitor.visit(this);
        }
    };

    public static void registerMethod(String name, Function<JSONObject, Method> constructor) {
        METHODS_MAP.put(name, constructor);
    }

    public static Method fromString(String methodStr, JSONObject params) {
        return METHODS_MAP.getOrDefault(methodStr, DEFAULT_METHOD).apply(params);
    }
}
