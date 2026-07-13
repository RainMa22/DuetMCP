package me.rainma22.DuetMCP.Methods;

import java.util.Map;
import me.rainma22.DuetMCP.UserContext;
import me.rainma22.DuetMCP.Exception.BadRequestException;
import me.rainma22.DuetMCP.Exception.DuetMCPException;
import me.rainma22.DuetMCP.Methods.notifications.Notification;
import me.rainma22.DuetMCP.Methods.notifications.NotificationProcessor;
import me.rainma22.DuetMCP.Tools.ToolFactory;
import me.rainma22.DuetMCP.Utils.ServerInfo;
import me.rainma22.DuetMCP.Utils.SessionManager;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 */
public class MethodEvaluator implements MethodVisitor<JSONObject, DuetMCPException> {

    private UserContext ctx;

    @Override
    public JSONObject visit(Method m) throws BadRequestException {
        throw new UnsupportedOperationException("method: Not supported yet.");
    }

    public MethodEvaluator(UserContext context) {
        ctx = context;
    }

    @Override
    public JSONObject visit(InitializeMethod im) throws DuetMCPException {
        JSONObject result = new JSONObject();
        try {
            var params = im.getParams();
            var protocolVerison = params.optString("protocolVersion");
            if (im.supportsVersion(protocolVerison)) {
                result.put("protocolVersion", protocolVerison);
                ctx.version = protocolVerison;
            } else {
                result.put("protocolVersion", im.latestVersion());
                ctx.version = im.latestVersion();
            }
            var clientCapabilities = params.optJSONObject("capabilities");
            if (clientCapabilities != null) {
                ctx.supportsRoots = clientCapabilities.opt("roots") != null;
                ctx.supportsSampling = clientCapabilities.opt("sampling") != null;
                ctx.supportsElicitation = clientCapabilities.opt("elicitation") != null;
            }
            SessionManager.getInstance().newSession(ctx);
            result.put("protocolVersion", ctx.version);
            result.put("capabilities", ServerInfo.CAPABILITY_JSON);
            result.put("serverInfo", ServerInfo.SERVER_INFO_JSON);

        } catch (JSONException je) {
            
            throw new BadRequestException(je);
        }
        return result;
    }

    @Override
    public JSONObject visit(Notification ni) throws DuetMCPException {
        NotificationProcessor np = new NotificationProcessor(ctx);
        return ni.accept(np);
    }

    @Override
    public JSONObject visit(PingMethod pm) throws DuetMCPException {
        return new JSONObject();
    }

    @Override
    public JSONObject visit(ToolsListMethod tlm) throws DuetMCPException {
        return new JSONObject(Map.of("tools", ToolFactory.availableTools()));
    }

    @Override
    public JSONObject visit(ToolsCallMethod tcm) throws DuetMCPException {
        var params = tcm.getParams();
        var args =  params.optJSONObject("arguments");
        return ToolFactory.fromString(params.getString("name")).apply(ctx, args);
    }

}
