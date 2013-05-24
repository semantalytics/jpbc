package it.unisa.dia.gas.plaf.jpbc.util.mt;

import java.util.concurrent.Callable;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public interface Filler {



    public interface FillerCallable<T> extends Callable<T> {

        void setIndex(int index);

    }

}
