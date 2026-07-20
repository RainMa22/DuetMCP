/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package me.rainma22.DuetMCP.mcphandler.subhandlers;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import me.rainma22.DuetMCP.Methods.MethodEvaluator;
import me.rainma22.DuetMCP.UserContext;
import me.rainma22.DuetMCP.Utils.JSONRPCCodes;
import me.rainma22.DuetMCP.Utils.MCPConstants;
import me.rainma22.DuetMCP.Utils.ResourceRegistries;
import me.rainma22.DuetMCP.requests.RequestProcessor;
import me.rainma22.DuetMCP.requests.Result;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 */
public class EventStreamSubHandler implements SubHandler {

    private static final System.Logger LOGGER = System.getLogger(EventStreamSubHandler.class.getName());

    private UserContext uctx;
    private RequestProcessor processor;

    public EventStreamSubHandler(ResourceRegistries rr, UserContext uctx) {
        this.uctx = uctx;
        this.processor = new RequestProcessor(new MethodEvaluator(rr,uctx));
    }
    //TODO: make this static var below configurable
    public static final int RETRY_MILLISECOND = 2200;
    //TODO: make this static var below configurable
    public static final int MAX_SPINWAIT_LOOPTIMES = 22;

    public static final String TERMINATION_NOTICE = "data: \n"
            + "retry: " + String.valueOf(RETRY_MILLISECOND)
            + "\n\n";
    private static final Map<String, String> RES_HEADER_MAP
            = Map.of(
                    "Content-Type", "text/event-stream",
                    "Cache-Control", "no-cache",
                    "Connection", "keep-alive",
                    "Transfer-Encoding", "chunked"
            );

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        InputStream in = exchange.getRequestBody();
        var id = uctx.sessionId.get();
        try {
            LOGGER.log(System.Logger.Level.INFO, "Received EventStream "
                    + "Request {0} from {1}", exchange.getRequestMethod(), id);
            if (exchange.getRequestMethod().equalsIgnoreCase("get")) {
                if (uctx.sessionId.refersTo(null)) {
                    exchange.sendResponseHeaders(JSONRPCCodes.HTTP_INVALID_REQUEST, 0);
                    LOGGER.log(System.Logger.Level.INFO, "Unregistered User attempted to get events");
                    exchange.close();
                    return;
                }
            } else {
                Result result;
                String reqString = new String(in.readAllBytes());
                if (reqString.replaceAll(" ", "").startsWith("[")) {
                    JSONArray arr = new JSONArray(reqString);
                    result = processor.handleMultiple(arr.toList().toArray());
                } else {
                    result = processor.handleSingular(new JSONObject(reqString));
                }
                if (result.resObj() != null) {
                    uctx.queueEvent(result.resObj().toString());
                }
            }// POST
            RES_HEADER_MAP.forEach((k, v)
                    -> exchange.getResponseHeaders().add(k, v));
            id = uctx.sessionId.get(); //update id in case of initialize
            if (id != null) {
                exchange.getResponseHeaders()
                        .add(MCPConstants.MCP_SESSION_ID_STRING,
                                id.toString());
            }
            exchange.sendResponseHeaders(JSONRPCCodes.HTTP_SUCCESS, 0);
            int spinwaitTimes = 0;
            OutputStream out = exchange.getResponseBody();
            while (++spinwaitTimes < MAX_SPINWAIT_LOOPTIMES) {
                while (uctx.eventQueue.hasEvent()) {
                    spinwaitTimes = 0; //reset spinwait times
                    var event = uctx.eventQueue.peek();
                    out.write(event.toString().getBytes("UTF-8"));
                    out.write("\n".getBytes("UTF-8"));
                    out.flush();
                    uctx.eventQueue.poll();
                }
                if (!uctx.sessionId.refersTo(null)) {
                    // if user context is freed/not initialized, don't ask it to loop.
                    break;
                }
                Thread.yield();
            }
            out.write(TERMINATION_NOTICE.getBytes("UTF-8"));
        } catch (Exception e) {
            LOGGER.log(System.Logger.Level.ERROR, "unknown exception received, closing connection...");
            LOGGER.log(System.Logger.Level.ERROR, null, "{0}", e);
        } finally {
            LOGGER.log(System.Logger.Level.INFO, "Kicked {0} from getting events.",
                    String.valueOf(uctx.sessionId.get()));
            exchange.close();
        }

    }

}
