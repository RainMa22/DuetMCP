package me.rainma22.DuetMCP.Tools;

import me.rainma22.DuetMCP.Exception.DuetMCPException;
import me.rainma22.DuetMCP.UserContext;
import me.rainma22.DuetMCP.interfaces.ThrowingBiFunction;
import org.json.JSONObject;

/**
 *
 */
public interface Tool extends ThrowingBiFunction<UserContext, JSONObject, JSONObject, DuetMCPException> {

}
