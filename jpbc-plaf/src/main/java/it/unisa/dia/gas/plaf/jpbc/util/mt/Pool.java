package it.unisa.dia.gas.plaf.jpbc.util.mt;

import java.util.concurrent.Callable;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.0.0
 */
public interface Pool<T> {

    Pool submit(Callable<T> callable);

    Pool submit(Runnable runnable);

    Pool<T> process();

}
