package me.rainma22.DuetMCP;

import org.json.JSONObject;

/**
 *
 */
public class Response extends JSONObject {

    private Response(String id) {
        super();
        put("jsonrpc", "2.0");
        put("id", id);
    }

    public static JSONObject ofSuccess(String id, JSONObject result) {
        var res = new Response(id);
        res.put("result", result);
        return res;
    }

    public static Response ofError(String id, String method, int errCode, String errormsg) {
        var res = new Response(id);
        var error = new JSONObject();
        res.put("method", method);

        error.put("code", errCode);
        error.put("message", errormsg);
        res.put("error", error);
        return res;
    }

}
