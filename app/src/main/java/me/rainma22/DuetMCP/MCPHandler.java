package me.rainma22.DuetMCP;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import me.rainma22.DuetMCP.Exception.BadRequestException;
import me.rainma22.DuetMCP.Methods.MethodEvaluator;
import me.rainma22.DuetMCP.Utils.JSONRPCCodes;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MCPHandler implements HttpHandler {

    private final System.Logger logger = System.getLogger(this.getClass().getName());

    private static record Result(int httpStatus, Object result) {

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
            response = Response.ofSuccess(id, request.getMethod().accept(evaluator));
        } catch (BadRequestException bre) {
            response = Response.ofError(obj.optString("id"), obj.optString("method"),
                    JSONRPCCodes.RPC_INVALID_PARAMS, bre.getMessage());
            httpStatus = JSONRPCCodes.HTTP_INVALID_PARAMS;
        } catch (JSONException je) {
            response = Response.ofError(null, null, JSONRPCCodes.RPC_PARSE_ERROR,
                    je.getMessage());
            httpStatus = JSONRPCCodes.HTTP_INVALID_REQUEST;
        } catch (UnsupportedOperationException uoe) {
            response = Response.ofError(null, null, JSONRPCCodes.RPC_SERVER_ERROR_GENERAL, uoe.getMessage());
            httpStatus = JSONRPCCodes.HTTP_SERVER_ERROR;
        } catch (Exception e){
//            e.printStackTrace();
            response = Response.ofError(null, null, JSONRPCCodes.RPC_SERVER_ERROR_GENERAL, e.getMessage());
            httpStatus = JSONRPCCodes.HTTP_SERVER_ERROR;
        }
        return new Result(httpStatus, response);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        InputStream reqBody = exchange.getRequestBody();
        Result response;
        MethodEvaluator evaluator = new MethodEvaluator(new UserContext());

        try {
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

            var outBytes = response.result.toString().getBytes("UTF-8");
            var outStream = exchange.getResponseBody();
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(response.httpStatus, outBytes.length);
            outStream.write(outBytes);
            outStream.close();
        } catch (IOException ie) {
            ie.printStackTrace(System.err);
        }
    }
}
