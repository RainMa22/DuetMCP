package me.rainma22.DuetMCP;

import me.rainma22.DuetMCP.Exception.BadRequestException;
import me.rainma22.DuetMCP.Methods.Method;
import me.rainma22.DuetMCP.Methods.MethodFactory;
import org.json.JSONObject;

/**
 *
 */
public class Request {

    private JSONObject obj;

    private Request(JSONObject jo) {
        obj = jo;
    }

    public static Request wrap(JSONObject req) throws BadRequestException {
        if (!"2.0".equals(req.optString("jsonrpc"))) {
            throw new BadRequestException("bad jsonrpc version, expected 2.0");
        }
        if (null == req.optString("id")) {
            throw new BadRequestException("ids cannot be null!");
        }
        if (null == req.optString("method")) {
            throw new BadRequestException("method cannot be null!");
        }
        return new Request(req);
    }

    public Method getMethod() {
        return MethodFactory.fromString(obj.getString("method"),
                getParams());
    }

    public String getId() {
        return obj.getString("id");
    }

    public JSONObject getParams() {
        var params = obj.optJSONObject("params", new JSONObject());
        return params;
    }
}
