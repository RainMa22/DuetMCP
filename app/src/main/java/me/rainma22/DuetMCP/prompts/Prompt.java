package me.rainma22.DuetMCP.prompts;

import me.rainma22.DuetMCP.Exception.DuetMCPException;
import me.rainma22.DuetMCP.interfaces.ThrowingBiFunction;
import me.rainma22.DuetMCP.UserContext;
import org.json.JSONObject;


/**
 *
 */
public interface Prompt extends ThrowingBiFunction<UserContext, JSONObject, JSONObject, DuetMCPException>{

}
