package it.unisa.dia.gas.plaf.jpbc.util.mt;

import java.util.concurrent.Callable;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public interface Accumulator<T> extends Pool<T> {

    Accumulator<T> accumulate(Callable<T> callable);

    Accumulator<T> process();

    T getResult();

}
