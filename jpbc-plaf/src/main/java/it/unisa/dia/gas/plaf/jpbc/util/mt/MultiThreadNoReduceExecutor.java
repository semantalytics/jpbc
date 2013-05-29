package it.unisa.dia.gas.plaf.jpbc.util.mt;

import java.util.concurrent.Executor;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class MultiThreadNoReduceExecutor<T> extends MultiThreadExecutor<T> {


    public MultiThreadNoReduceExecutor() {
    }

    public MultiThreadNoReduceExecutor(Executor executor) {
        super(executor);
    }


    public void reduce(T value) {
    }

}
