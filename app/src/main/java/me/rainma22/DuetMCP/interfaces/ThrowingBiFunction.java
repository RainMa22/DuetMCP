package me.rainma22.DuetMCP.interfaces;

/**
 *
 */
public interface ThrowingBiFunction<P1, P2,R,E extends Exception> {
    R apply(P1 param1, P2 param2) throws E;
}
