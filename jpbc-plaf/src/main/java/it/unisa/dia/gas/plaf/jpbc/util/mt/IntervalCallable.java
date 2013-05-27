package it.unisa.dia.gas.plaf.jpbc.util.mt;

import java.util.concurrent.Callable;

/**
* @author Angelo De Caro (angelo.decaro@gmail.com)
* @since 1.3.0
*/
public abstract class IntervalCallable<T> implements Callable<T> {
    protected int from, to;

    protected IntervalCallable(int from, int to) {
        this.from = from;
        this.to = to;
    }
}
