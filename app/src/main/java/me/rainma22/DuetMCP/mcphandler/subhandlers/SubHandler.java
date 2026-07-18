package me.rainma22.DuetMCP.mcphandler.subhandlers;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

/**
 *
 */
public interface SubHandler {
    void handle(HttpExchange exchange) throws IOException;
}
