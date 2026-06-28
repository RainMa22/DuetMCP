package me.rainma22.DuetMCP.Methods;

import java.util.List;
import org.json.JSONObject;

public class InitializeMethod extends Method {
    

    public InitializeMethod(JSONObject params) {
        super(params);
    }
    
    @Override
    public <T, E extends Exception> T accept(MethodVisitor<T, E> visitor) throws E {
        return visitor.visit(this);
    }

    public boolean supportsVersion(String versionString) {
        return SUPPORTED_VERSIONS.contains(versionString);
    }

    public String latestVersion() {
        return SUPPORTED_VERSIONS.getLast();
    }
    private static final List<String> SUPPORTED_VERSIONS = List.of(
            "2024-11-05",
            "2025-03-26",
            "2025-06-18",
            "2025-11-25",
            "2026-07-28");

}
