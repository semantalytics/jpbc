package it.unisa.dia.gas.plaf.jpbc.util.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class ExecutorServiceUtils {

    private static ExecutorService executorService;

    static {
//        executorService = Executors.newCachedThreadPool();
        executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors() * 10
        );
    }


    private ExecutorServiceUtils() {
    }


    public static ExecutorService getExecutorService() {
        return executorService;
    }

    /**
     * @author Angelo De Caro (angelo.decaro@gmail.com)
     * @since 1.3.0
     */
    public abstract static class IndexCallable<T> implements Callable<T> {
        protected int i, j;

        public IndexCallable(int i) {
            this.i = i;
        }

        public IndexCallable(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }

    /**
     * @author Angelo De Caro (angelo.decaro@gmail.com)
     * @since 1.2.2
     */
    public abstract static class IndexRunnable implements Runnable {
        protected int i, j;

        public IndexRunnable(int i) {
            this.i = i;
        }

        public IndexRunnable(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }

    /**
     * @author Angelo De Caro (angelo.decaro@gmail.com)
     * @since 1.3.0
     */
    public abstract static class IntervalCallable<T> implements Callable<T> {
        protected int from, to;

        protected IntervalCallable(int from, int to) {
            this.from = from;
            this.to = to;
        }
    }

}
