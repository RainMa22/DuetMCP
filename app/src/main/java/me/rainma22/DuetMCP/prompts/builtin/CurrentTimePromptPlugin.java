package me.rainma22.DuetMCP.prompts.builtin;

import java.util.List;
import java.util.Map;
import me.rainma22.DuetMCP.Exception.DuetMCPException;
import me.rainma22.DuetMCP.UserContext;
import me.rainma22.DuetMCP.Utils.ResourceRegistries;
import me.rainma22.DuetMCP.Utils.Tuple;
import me.rainma22.DuetMCP.prompts.Prompt;
import me.rainma22.DuetMCP.prompts.PromptPlugin;
import org.json.JSONObject;

/**
 *
 */
public class CurrentTimePromptPlugin implements PromptPlugin {

    private class CurrentTimePrompt implements Prompt {

        @Override
        public JSONObject apply(UserContext ignored, JSONObject args) throws DuetMCPException {
            String timezone = args.optString("Timezone", null);
            String prompt = "What is the current time in yyyy-MM-dd format";
            if (timezone != null) {
                prompt += " in " + timezone + "?";
            } else {
                prompt += "?";
            }
            return new JSONObject(
                    Map.of("description", "get current datetime prompt",
                            "messages", List.of(
                                    Map.of("role", "user",
                                            "content", Map.of("type", "text",
                                                    "text", prompt
                                            )))));
        }
    }
    final Map<String, Object> INFO = Map.of(
            "name", "duet_getTimePrompt",
            "title", "get current time",
            "description", "prompt for the current time in yyyy-MM-dd format",
            "inputSchema", Map.of(
                    "type", "object",
                    "arguments", List.of(
                            Map.of("name", "Timezone",
                                    "description", "a timezone name",
                                    "required", false)))
    );

    @Override
    public void onLoad(ResourceRegistries registries) {

        registries.promptRegistry().register(INFO.get("name").toString(),
                new Tuple<>(INFO, new CurrentTimePrompt()));
    }

}
