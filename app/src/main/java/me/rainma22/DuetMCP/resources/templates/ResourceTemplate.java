package me.rainma22.DuetMCP.resources.templates;

import me.rainma22.DuetMCP.Exception.DuetMCPException;
import me.rainma22.DuetMCP.UserContext;
import me.rainma22.DuetMCP.interfaces.ThrowingBiFunction;
import org.json.JSONObject;

/**
 *
 */
interface ResourceTemplate extends ThrowingBiFunction<UserContext, JSONObject, JSONObject, DuetMCPException>{

}
