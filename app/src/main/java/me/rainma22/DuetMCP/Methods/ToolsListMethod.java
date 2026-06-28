package me.rainma22.DuetMCP.Methods;

import org.json.JSONObject;

/**
 *
 */
public class ToolsListMethod extends Method {
    
    public ToolsListMethod(JSONObject param) {
        super(param);
    }
    
    @Override
    public <T, E extends Exception> T accept(MethodVisitor<T, E> visitor) throws E {
        return visitor.visit(this);
    }
    
}
