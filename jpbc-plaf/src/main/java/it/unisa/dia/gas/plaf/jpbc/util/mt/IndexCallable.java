package it.unisa.dia.gas.plaf.jpbc.util.mt;

import java.util.concurrent.Callable;

/**
* @author Angelo De Caro (angelo.decaro@gmail.com)
* @since 1.3.0
*/
public abstract class IndexCallable<T> implements Callable<T> {
    protected int i, j;

    public IndexCallable(int i) {
        this.i = i;
    }

    public IndexCallable(int i, int j) {
        this.i = i;
        this.j = j;
    }
}
