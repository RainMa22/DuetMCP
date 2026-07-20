package me.rainma22.DuetMCP.mcphandler.subhandlers;

import java.util.List;
import me.rainma22.DuetMCP.UserContext;
import me.rainma22.DuetMCP.Utils.ResourceRegistries;

/**
 * Routes the exchange to different handlers depending how the endpoint is
 * called (thanks MCP spec...)
 */
public class SubHandlerRouter {

    private ResourceRegistries rr;

    public SubHandlerRouter(ResourceRegistries rr) {
        this.rr = rr;
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
                    return new EventStreamSubHandler(rr, uctx);
                } else {
//            attempt fallback to json, would error out if json is invalid
                    return new JsonSubHandler(rr, uctx);
                }
            case "get":
                return new EventStreamSubHandler(rr, uctx);
        }

    }

}
