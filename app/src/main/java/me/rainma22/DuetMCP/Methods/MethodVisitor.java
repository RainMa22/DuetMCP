package me.rainma22.DuetMCP.Methods;

/**
 *
 */
public interface MethodVisitor<T, E extends Exception> {

    T visit(Method m) throws E;

    T visit(InitializeMethod im) throws E;

    T visit(NotificationInitialized ni) throws E;

    T visit(PingMethod pm) throws E;

    T visit(ToolsListMethod tlm) throws E;

    T visit(ToolsCallMethod tcm) throws E;

}
