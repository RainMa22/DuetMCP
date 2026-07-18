package me.rainma22.DuetMCP.mcphandler.subhandlers;

import java.util.List;
import me.rainma22.DuetMCP.UserContext;

/**
 * Routes the exchange to different handlers depending
 * how the endpoint is called (thanks MCP spec...)
 */
public class SubHandlerRouter {

    public SubHandlerRouter() {
    }

    public SubHandler route(String method, List<String> acceptList,
            UserContext uctx) {
        switch (method.toLowerCase()) {
            default:
            case "options":
                return new OptionsSubHandler();
            case "post":
                //prefer event stream over JSONRPC as it appears to be more efficient
                if (acceptList.contains("text/event-stream")) {
                    return new EventStreamSubHandler(uctx);
                } else {
//            attempt fallback to json, would error out if json is invalid
                    return new JsonSubHandler(uctx);
                }
            case "get":
                return new EventStreamSubHandler(uctx);
        }

    }

}
