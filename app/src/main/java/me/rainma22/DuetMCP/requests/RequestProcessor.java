package me.rainma22.DuetMCP.requests;

import me.rainma22.DuetMCP.Exception.BadRequestException;
import me.rainma22.DuetMCP.Exception.DuetMCPException;
import me.rainma22.DuetMCP.Methods.MethodEvaluator;
import me.rainma22.DuetMCP.Utils.JSONRPCCodes;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 */
public class RequestProcessor {

    private static final System.Logger LOGGER = System.getLogger(RequestProcessor.class.getName());
    private MethodEvaluator evaluator;

    public RequestProcessor(MethodEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    public Result handleMultiple(Object... objs) {
        var out = new JSONArray();
        for (Object obj : objs) {
            try {
                Object singularResult = handleSingular(obj).resObj();
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
        return new Result(JSONRPCCodes.HTTP_SUCCESS, out);
    }

    public Result handleSingular(Object object) {
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
                httpStatus = JSONRPCCodes.HTTP_ACCEPTED;
                response = null;
            }
        } catch (BadRequestException bre) {
            LOGGER.log(System.Logger.Level.WARNING, "Bad Request Received: {0}", bre);
            response = Response.ofError(obj.optString("id"), obj.optString("method"),
                    JSONRPCCodes.RPC_INVALID_PARAMS, bre.getMessage());
            httpStatus = JSONRPCCodes.HTTP_INVALID_PARAMS;
        } catch (JSONException je) {
            LOGGER.log(System.Logger.Level.WARNING, "Exception When Parsing JSON: {0}", je);
            response = Response.ofError(null, null, JSONRPCCodes.RPC_PARSE_ERROR,
                    je.getMessage());
            httpStatus = JSONRPCCodes.HTTP_INVALID_REQUEST;
        } catch (DuetMCPException | UnsupportedOperationException uoe) {
            LOGGER.log(System.Logger.Level.WARNING, "Received MCP Server Exception: {0}", uoe);
            response = Response.ofError(null, null, JSONRPCCodes.RPC_SERVER_ERROR_GENERAL, uoe.getMessage());
            httpStatus = JSONRPCCodes.HTTP_SERVER_ERROR;
        } catch (Exception e) {
            LOGGER.log(System.Logger.Level.ERROR, "{0}", e);
            response = Response.ofError(null, null, JSONRPCCodes.RPC_SERVER_ERROR_GENERAL, e.getMessage());
            httpStatus = JSONRPCCodes.HTTP_SERVER_ERROR;
        }
        return new Result(httpStatus, response);
    }

}
