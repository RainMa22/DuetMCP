package me.rainma22.DuetMCP;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;
import me.rainma22.DuetMCP.Exception.BadRequestException;
import me.rainma22.DuetMCP.Exception.DuetMCPException;
import me.rainma22.DuetMCP.Methods.MethodEvaluator;
import me.rainma22.DuetMCP.Utils.JSONRPCCodes;
import me.rainma22.DuetMCP.Utils.MCPConstants;
import me.rainma22.DuetMCP.Utils.SessionManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * MCP endpoint base-class Routes the exchange to different handlers depending
 * how the endpoint is called (thanks MCP spec...)
 */
public class MCPHandler implements HttpHandler {

    private final System.Logger logger = System.getLogger(this.getClass().getName());
    private static final Map<String, String> CORS_HEADER = Map.of(
            "Access-Control-Allow-Origin", "*",
            "Access-Control-Allow-Methods", "GET, POST, OPTIONS",
            "Access-Control-Allow-Headers", "*",
            "Access-Control-Max-Age", "86400");

    private static record Result(int httpStatus, Object result) {

    }

    private class EventStreamSubHandler implements HttpHandler {

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

            String id = exchange.getRequestHeaders()
                    .getFirst(MCPConstants.MCP_SESSION_ID_STRING);
            UserContext uctx = SessionManager.getInstance().getContextOf(id)
                    .orElse(new UserContext());
            InputStream in = exchange.getRequestBody();
            OutputStream out = exchange.getResponseBody();
            try {
                logger.log(System.Logger.Level.INFO, "Received EventStream "
                        + "Request {0} from {1}", exchange.getRequestMethod(), id);
                if (exchange.getRequestMethod().equalsIgnoreCase("get")) {
                    if (uctx.sessionId.refersTo(null)) {
                        exchange.sendResponseHeaders(JSONRPCCodes.HTTP_INVALID_REQUEST, 0);
                        logger.log(System.Logger.Level.INFO, "Unregistered User attempted to get events");
                        exchange.close();
                        return;
                    }
                } else {
                    var evaluator = new MethodEvaluator(uctx);
                    String reqString = new String(in.readAllBytes());
                    Result response = handleSingular(evaluator, new JSONObject(reqString));
                    if (response.result != null) {
                        uctx.queueEvent(response.result.toString());
                    }
                }// POST
                RES_HEADER_MAP.forEach((k, v)
                        -> exchange.getResponseHeaders().add(k, v));
                var uid = uctx.sessionId.get(); 
                if (uid != null) {
                    exchange.getRequestHeaders()
                            .add(MCPConstants.MCP_SESSION_ID_STRING, 
                                    uid.toString());
                }
                exchange.sendResponseHeaders(JSONRPCCodes.HTTP_SUCCESS, 0);
                int spinwaitTimes = 0;
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
                logger.log(System.Logger.Level.ERROR, "unknown exception received, closing connection...");
                logger.log(System.Logger.Level.ERROR, null, "{0}", e);
            } finally {
                logger.log(System.Logger.Level.INFO, "Kicked {0} from getting events.",
                        String.valueOf(uctx.sessionId.get()));
                exchange.close();
            }

        }

    }

    private class JsonSubHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) {
            var reqHeaders = exchange.getRequestHeaders();
            InputStream reqBody = exchange.getRequestBody();
            Result response;

            try {
                String sessionStr = reqHeaders.getFirst(MCPConstants.MCP_SESSION_ID_STRING);

                UserContext ctx = SessionManager.getInstance().getContextOf(sessionStr)
                        .orElse(new UserContext());
                MethodEvaluator evaluator = new MethodEvaluator(ctx);
                response = new Result(JSONRPCCodes.HTTP_SUCCESS, null);
                String reqString = new String(reqBody.readAllBytes());
                logger.log(System.Logger.Level.INFO, "received:" + reqString);
                if (reqString.replaceAll(" ", "").startsWith("[")) {
                    JSONArray arr = new JSONArray(reqString);
                    JSONArray out = new JSONArray();
                    for (int i = 0; i < arr.length(); i++) {
                        try {
                            Object singularResult = handleSingular(evaluator, arr.get(i)).result;
                            if (singularResult == null) {
                                continue;
                            }
                            out.put(singularResult);
                        } catch (JSONException je) {
                            out.put(Response.ofError(null, null,
                                    JSONRPCCodes.RPC_PARSE_ERROR,
                                    je.getMessage()));
                        }
                    }
                    response = new Result(JSONRPCCodes.HTTP_SUCCESS, out);
                } else {
                    response = handleSingular(evaluator, new JSONObject(reqString));
                }
                logger.log(System.Logger.Level.INFO, "result:" + response.result);
                var outBytes = (response.result == null) ? new byte[0] : response.result.toString().getBytes("UTF-8");
                var outStream = exchange.getResponseBody();
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                if (sessionStr != null) {
                    exchange.getResponseHeaders().set(MCPConstants.MCP_SESSION_ID_STRING,
                            sessionStr);
                }
                exchange.sendResponseHeaders(response.httpStatus, outBytes.length);
                outStream.write(outBytes);
                outStream.flush();
            } catch (Exception e) {
                logger.log(System.Logger.Level.ERROR, "ERROR!");
                logger.log(System.Logger.Level.ERROR, null, "{0}", e);
            }
            exchange.close();
        }
    }

    private EventStreamSubHandler eventStreamHandler = new EventStreamSubHandler();
    private JsonSubHandler jsonHandler = new JsonSubHandler();

//    https://modelcontextprotocol.io/specification/2025-11-25/basic/transports#:~:text=Servers%20MUST%20validate%20the%20Origin%20header%20on%20all%20incoming%20connections%20to%20prevent%20DNS%20rebinding%20attacks
//    TODO: validate ORIGIN
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String reqMethod = exchange.getRequestMethod().toLowerCase();
        var reqHeaders = exchange.getRequestHeaders();
        CORS_HEADER.forEach((k, v) -> exchange.getResponseHeaders().add(k, v));
        if (reqMethod.equals("options")) {
            exchange.sendResponseHeaders(JSONRPCCodes.HTTP_SUCCESS, 0);
            exchange.close();
            return;
        }
        var acceptList = reqHeaders.getOrDefault("Accept", List.of());
//        StringJoiner sj = new StringJoiner("\n");
//        reqHeaders.forEach((k, vs) -> vs.forEach((v) -> sj.add(k + ": " + v)));
//        logger.log(System.Logger.Level.INFO, "Received {0} request with Header {1}",
//                reqMethod, sj);
        //prefer event stream over JSONRPC as it appears to be more efficient
        if (acceptList.contains("text/event-stream")) {
            eventStreamHandler.handle(exchange);
            return;
        } else {
//            attempt fallback to json, would error out if json is invalid
            jsonHandler.handle(exchange);
            return;
        }

    }

    private Result handleSingular(MethodEvaluator evaluator, Object object) {
        JSONObject response = new JSONObject();
        JSONObject obj = new JSONObject();

        int httpStatus = JSONRPCCodes.HTTP_SUCCESS;
        try {
            if (object instanceof JSONObject) {
                obj = (JSONObject) object;
            } else {
                throw new JSONException("Not a json object: " + object.toString());
            }
            Request request = Request.wrap(obj);
            String id = obj.optString("id");
            var reqEvaluation = request.getMethod().accept(evaluator);
            if (reqEvaluation != null) {
                response = Response.ofSuccess(id, reqEvaluation);
            } else {
                httpStatus = HttpURLConnection.HTTP_ACCEPTED;
                response = null;
            }
        } catch (BadRequestException bre) {
            response = Response.ofError(obj.optString("id"), obj.optString("method"),
                    JSONRPCCodes.RPC_INVALID_PARAMS, bre.getMessage());
            httpStatus = JSONRPCCodes.HTTP_INVALID_PARAMS;
        } catch (JSONException je) {
            response = Response.ofError(null, null, JSONRPCCodes.RPC_PARSE_ERROR,
                    je.getMessage());
            httpStatus = JSONRPCCodes.HTTP_INVALID_REQUEST;
        } catch (DuetMCPException | UnsupportedOperationException uoe) {
            response = Response.ofError(null, null, JSONRPCCodes.RPC_SERVER_ERROR_GENERAL, uoe.getMessage());
            httpStatus = JSONRPCCodes.HTTP_SERVER_ERROR;
        } catch (Exception e) {
//            e.printStackTrace();
            response = Response.ofError(null, null, JSONRPCCodes.RPC_SERVER_ERROR_GENERAL, e.getMessage());
            httpStatus = JSONRPCCodes.HTTP_SERVER_ERROR;
        }
        return new Result(httpStatus, response);
    }

}
