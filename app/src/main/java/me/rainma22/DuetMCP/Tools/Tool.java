package me.rainma22.DuetMCP.Tools;

import me.rainma22.DuetMCP.Exception.BadRequestException;
import me.rainma22.DuetMCP.Functions.ThrowingBiFunction;
import me.rainma22.DuetMCP.UserContext;
import org.json.JSONObject;

/**
 *
 */
public abstract class Tool implements ThrowingBiFunction<UserContext, JSONObject, JSONObject, BadRequestException> {

    public abstract void onLoad();
}
