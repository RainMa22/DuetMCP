package me.rainma22.DuetMCP.Methods.notifications;

import me.rainma22.DuetMCP.Methods.MethodVisitor;
import org.json.JSONObject;

/**
 *
 */
public class NotificationInitialized extends Notification {

    public NotificationInitialized(JSONObject param) {
        super(param);
    }

    @Override
    public <T, E extends Exception> T accept(NotificationVisitor<T, E> visitor) throws E {
        return visitor.visit(this);
    }
}
