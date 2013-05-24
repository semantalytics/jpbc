package it.unisa.dia.gas.plaf.jpbc.util.mt;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public abstract class MultiThreadExecutor<T> implements Pool<T> {

    private CompletionService<T> pool;
    private int counter;


    protected MultiThreadExecutor() {
        this.pool = new ExecutorCompletionService<T>(MTUtils.executorService);
        this.counter = 0;
    }


    public Pool submit(Callable<T> callable) {
        counter++;
        pool.submit(callable);

        return this;
    }

    public Pool submit(Runnable runnable) {
        counter++;
        pool.submit(runnable, null);

        return this;
    }

    public Pool<T> process(){
        try{
            for(int i = 0; i < counter; i++)
                reduce(pool.take().get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            counter = 0;
        }
        return this;
    }

    public abstract void reduce(T value);


    public static abstract class IndexRunnable implements Runnable {
        protected int i, j;

        protected IndexRunnable(int i) {
            this.i = i;
        }

        protected IndexRunnable(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }

}
