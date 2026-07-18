package me.rainma22.DuetMCP.mcphandler.subhandlers;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import me.rainma22.DuetMCP.Utils.JSONRPCCodes;

/**
 * Dumb response Handler
 */
public class OptionsSubHandler implements SubHandler{

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(JSONRPCCodes.HTTP_SUCCESS, 0);
        exchange.close();
    }
    
}
