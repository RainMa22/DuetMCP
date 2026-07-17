package me.rainma22.DuetMCP.Methods;

import java.util.List;
import static me.rainma22.DuetMCP.Utils.ServerInfo.SUPPORTED_VERSIONS;
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

}
