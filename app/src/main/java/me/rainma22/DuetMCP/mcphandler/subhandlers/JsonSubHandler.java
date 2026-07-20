package me.rainma22.DuetMCP.mcphandler.subhandlers;

import com.sun.net.httpserver.HttpExchange;
import java.io.InputStream;
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
public class JsonSubHandler implements SubHandler {
    private static final System.Logger LOGGER = System.getLogger(JsonSubHandler.class.getName());

    private UserContext uctx;
    private RequestProcessor processor;

    public JsonSubHandler(ResourceRegistries rr, UserContext uctx) {
        this.uctx = uctx;
        this.processor = new RequestProcessor(new MethodEvaluator(rr, uctx));
    }

    @Override
    public void handle(HttpExchange exchange) {
        InputStream reqBody = exchange.getRequestBody();
        Result result;

        try {
            result = new Result(JSONRPCCodes.HTTP_SUCCESS, null);
            String reqString = new String(reqBody.readAllBytes());
            LOGGER.log(System.Logger.Level.INFO, "received:" + reqString);
            if (reqString.replaceAll(" ", "").startsWith("[")) {
                JSONArray arr = new JSONArray(reqString);
                result = processor.handleMultiple(arr.toList().toArray());
            } else {
                result = processor.handleSingular(new JSONObject(reqString));
            }
            var uid = uctx.sessionId.get();
            if (uid != null) {
                exchange.getResponseHeaders()
                        .add(MCPConstants.MCP_SESSION_ID_STRING,
                                uid.toString());
            }
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            var resObj = result.resObj();
            LOGGER.log(System.Logger.Level.INFO, "result:" + resObj);
            var outBytes = (result.resObj() == null) ? new byte[0] : resObj.toString().getBytes("UTF-8");
            
            exchange.sendResponseHeaders(result.httpStatus(), outBytes.length);

            var outStream = exchange.getResponseBody();
            outStream.write(outBytes);
            outStream.flush();
        } catch (Exception e) {
            LOGGER.log(System.Logger.Level.ERROR, "ERROR!");
            LOGGER.log(System.Logger.Level.ERROR, null, "{0}", e);
        }
        exchange.close();
    }
}
