package it.unisa.dia.gas.plaf.jpbc.util.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public abstract class PoolExecutor<T> implements Pool<T> {

    private CompletionService<T> pool;
    private int counter;


    protected PoolExecutor() {
        this(ExecutorServiceUtils.getExecutorService());
    }

    protected PoolExecutor(Executor executor) {
        this.pool = new ExecutorCompletionService<T>(executor);
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

    public Pool<T> process() {
        try {
            for (int i = 0; i < counter; i++)
                reduce(pool.take().get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            counter = 0;
        }
        return this;
    }

    protected abstract void reduce(T value);

}
