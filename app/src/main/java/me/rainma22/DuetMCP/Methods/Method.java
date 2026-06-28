package me.rainma22.DuetMCP.Methods;

import org.json.JSONObject;

public abstract class Method {

    protected JSONObject params;

    public Method(JSONObject param) {
        params = param;
    }

    public abstract <T, E extends Exception> T accept(MethodVisitor<T, E> visitor) throws E;

    public JSONObject getParams() {
        return params;
    }

}
