package me.rainma22.DuetMCP.Methods;

import org.json.JSONObject;

/**
 *
 */
public class PingMethod extends Method {

    public PingMethod(JSONObject param) {
        super(param);
    }

    @Override
    public <T, E extends Exception> T accept(MethodVisitor<T, E> visitor) throws E {
        return visitor.visit(this);
    }

}
