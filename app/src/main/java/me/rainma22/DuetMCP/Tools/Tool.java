package me.rainma22.DuetMCP.Tools;

import me.rainma22.DuetMCP.Exception.BadRequestException;
import me.rainma22.DuetMCP.UserContext;
import me.rainma22.DuetMCP.Functions.ThrowingBiFunction;
import org.json.JSONObject;

/**
 *
 */
public interface Tool extends ThrowingBiFunction<UserContext, JSONObject, JSONObject, BadRequestException> {

}
