package me.rainma22.DuetMCP.mcphandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import me.rainma22.DuetMCP.UserContext;
import me.rainma22.DuetMCP.Utils.MCPConstants;
import me.rainma22.DuetMCP.Utils.ResourceRegistries;
import me.rainma22.DuetMCP.Utils.SessionManager;
import me.rainma22.DuetMCP.mcphandler.subhandlers.SubHandlerRouter;

/**
 * MCP endpoint base-class
 */
public class MCPHandler implements HttpHandler {
    private SubHandlerRouter subHandlerRouter;
    public MCPHandler(ResourceRegistries rr){
        subHandlerRouter = new SubHandlerRouter(rr);
    }
//    private final System.Logger logger = System.getLogger(this.getClass().getName());
    private static final Map<String, String> CORS_HEADER = Map.of(
            "Access-Control-Allow-Origin", "*",
            "Access-Control-Allow-Methods", "GET, POST, OPTIONS",
            "Access-Control-Allow-Headers", "*",
            "Access-Control-Max-Age", "86400");

//    https://modelcontextprotocol.io/specification/2025-11-25/basic/transports#:~:text=Servers%20MUST%20validate%20the%20Origin%20header%20on%20all%20incoming%20connections%20to%20prevent%20DNS%20rebinding%20attacks
//    TODO: validate ORIGIN
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String reqMethod = exchange.getRequestMethod().toLowerCase();
        var reqHeaders = exchange.getRequestHeaders();
        CORS_HEADER.forEach((k, v) -> exchange.getResponseHeaders().add(k, v));
        String sessionStr = reqHeaders.getFirst(MCPConstants.MCP_SESSION_ID_STRING);
        UserContext ctx = SessionManager.getInstance().getContextOf(sessionStr)
                .orElse(new UserContext());
        var acceptList = reqHeaders.getOrDefault("Accept", List.of());
        subHandlerRouter.route(reqMethod, acceptList, ctx)
                .handle(exchange);
    }
}
