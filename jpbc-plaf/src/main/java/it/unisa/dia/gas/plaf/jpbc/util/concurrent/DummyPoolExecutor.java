package it.unisa.dia.gas.plaf.jpbc.util.concurrent;

import java.util.concurrent.Executor;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class DummyPoolExecutor<T> extends PoolExecutor<T> {


    public DummyPoolExecutor() {
    }

    public DummyPoolExecutor(Executor executor) {
        super(executor);
    }


    public void reduce(T value) {
    }

}
