package me.rainma22.DuetMCP.Methods;

import org.json.JSONObject;

/**
 *
 */
public class PromptGetMethod extends Method {

    public PromptGetMethod(JSONObject param) {
        super(param);
    }

    @Override
    public <T, E extends Exception> T accept(MethodVisitor<T, E> visitor) throws E {
        return visitor.visit(this);
    }

}
