package me.rainma22.DuetMCP.Methods.notifications;

/**
 *
 */
public interface NotificationVisitor<T, E extends Exception> {
    T visit(Notification n) throws E;
    T visit(NotificationInitialized ni) throws E;
}
