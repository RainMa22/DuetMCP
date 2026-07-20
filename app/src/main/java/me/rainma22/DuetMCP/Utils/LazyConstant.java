package me.rainma22.DuetMCP.Utils;

import java.util.function.Supplier;

/**
 *
 */
public class LazyConstant<T> {
    private T instance = null;
    private Supplier<? extends T> supplier;
    
    public LazyConstant(Supplier<? extends T> supplier){
        this.supplier = supplier;
    }
    public T get(){
        if(this.instance == null){
            instance = supplier.get();
        }
        return instance;
    }

}
