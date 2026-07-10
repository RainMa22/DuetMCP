package me.rainma22.DuetMCP.Methods;

import me.rainma22.DuetMCP.Methods.notifications.Notification;

/**
 *
 */
public interface MethodVisitor<T, E extends Exception> {

    T visit(Method m) throws E;

    T visit(InitializeMethod im) throws E;

    T visit(Notification ni) throws E;

    T visit(PingMethod pm) throws E;

    T visit(ToolsListMethod tlm) throws E;

    T visit(ToolsCallMethod tcm) throws E;

}
