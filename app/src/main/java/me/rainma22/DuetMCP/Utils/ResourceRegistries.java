package me.rainma22.DuetMCP.Utils;

import me.rainma22.DuetMCP.Tools.ToolRegistry;
import me.rainma22.DuetMCP.prompts.PromptRegistry;

/**
 *
 */
public record ResourceRegistries(PromptRegistry promptRegistry, 
        ToolRegistry toolRegistry) {

}
