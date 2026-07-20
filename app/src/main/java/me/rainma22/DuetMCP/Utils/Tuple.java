package me.rainma22.DuetMCP.Utils;

/**
 *
 */
public class Tuple<T, E> {

    private T first;
    private E second;

    public T first() {
        return first;
    }

    public E second() {
        return second;
    }

    public Tuple(T t, E e) {
        first = t;
        second = e;
    }

}
