package me.rainma22.DuetMCP.Methods.notifications;

import me.rainma22.DuetMCP.Methods.Method;
import me.rainma22.DuetMCP.Methods.MethodVisitor;
import org.json.JSONObject;

/**
 *
 */
public abstract class Notification extends Method {
    public Notification(JSONObject param) {
        super(param);
    }
    public abstract <T, E extends Exception> T accept(NotificationVisitor<T, E> visitor) throws E;

    @Override
    public <T, E extends Exception> T accept(MethodVisitor<T, E> visitor) throws E {
        return visitor.visit(this);
    }
}
