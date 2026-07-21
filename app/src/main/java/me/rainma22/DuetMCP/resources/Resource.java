package me.rainma22.DuetMCP.resources;

import java.lang.ref.Cleaner;
import java.util.HashSet;
import java.util.Set;
import me.rainma22.DuetMCP.Exception.DuetMCPException;
import me.rainma22.DuetMCP.UserContext;
import me.rainma22.DuetMCP.Utils.SessionManager;
import me.rainma22.DuetMCP.interfaces.ThrowingBiFunction;
import org.json.JSONObject;

/**
 *
 */
public abstract class Resource implements ThrowingBiFunction<UserContext, JSONObject, JSONObject, DuetMCPException> {

    private static final String RES_UPDATED_NOTIF = "notification/resources/updated";

    private final Cleaner cleaner = Cleaner.create();
    Set<UserContext> subscribers = new HashSet<>();

    public void subscribe(String seshid) {
        SessionManager.getInstance().getContextOf(seshid)
                .ifPresent((ctx) -> {
                    subscribers.add(ctx);
                    // TODO: avoid the coupling with the gc, 
                    //       it **should** work but **should** is not good enough
                    cleaner.register(ctx.sessionId.get(), () -> subscribers.remove(ctx));
                });
    }

    public void onUpdate() {
        subscribers.stream()
                .forEach((ctx) -> ctx.queueEvent(RES_UPDATED_NOTIF));
    }
// An example file-based Resource.apply(...) shown below:
//    @Override
//    public JSONObject apply(UserContext param1, JSONObject param2) throws DuetMCPException {
//        try (var res = info.getUri().toURL().openStream()) {
//            byte[] data = res.readAllBytes();
//            String mimeTypeStr = info.getMimeType() == null ? "" : info.getMimeType().toLowerCase();
//            JSONObject content = new JSONObject();
//            content.put("uri", info.getUri().toString());
//            content.put("mimeType", info.getMimeType());
//
//            if (mimeTypeStr.startsWith("text")) {
//                content.put("text", new String(data, "UTF-8"));
//            } else {
//                Encoder en = Base64.getEncoder();
//                content.put("blob", en.encode(data));
//            }
//            return new JSONObject(
//                    Map.of("Contents", List.of(
//                            content)));
//        } catch (Exception e) {
//            throw new DuetMCPException(e);
//        }
//    }
}
