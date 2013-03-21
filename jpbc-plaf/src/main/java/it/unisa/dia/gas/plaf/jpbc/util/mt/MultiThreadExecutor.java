package it.unisa.dia.gas.plaf.jpbc.util.mt;

import java.util.concurrent.*;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.2.1
 */
public abstract class MultiThreadExecutor<T> {

    static ExecutorService executorService;
    static {
        executorService = Executors.newCachedThreadPool();
    }

    private CompletionService<T> pool;
    private int counter;


    protected MultiThreadExecutor() {
        this.pool = new ExecutorCompletionService<T>(executorService);
        this.counter = 0;
    }


    public MultiThreadExecutor submit(Callable<T> callable) {
        counter++;
        pool.submit(callable);

        return this;
    }


    public void process(){
        try{
            for(int i = 0; i < counter; i++)
                reduce(pool.take().get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            counter = 0;
        }
    }

    public abstract void reduce(T value);

}
