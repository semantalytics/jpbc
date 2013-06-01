package it.unisa.dia.gas.plaf.jpbc.util.concurrent.accumultor;

import it.unisa.dia.gas.plaf.jpbc.util.concurrent.Pool;

import java.util.concurrent.Callable;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public interface Accumulator<T> extends Pool<T> {

    Accumulator<T> accumulate(Callable<T> callable);

    Accumulator<T> process();

    T getResult();

    T doFinal();

}
