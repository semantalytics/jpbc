package it.unisa.dia.gas.plaf.jpbc.util.mt;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class TaskManager {

    static ExecutorService executorService;
    static {
        executorService = Executors.newCachedThreadPool();
    }


    private CompletionService pool;
    private int counter;
    private Map<String, ValueLatch> context;
    private Map<String, Object> view;

    public TaskManager() {
        this.pool = new ExecutorCompletionService(executorService);
        this.counter = 0;
        this.context = Collections.synchronizedMap(new ValueLatchMap());
        this.view = new HashMap<String, Object>();
    }

    public TaskManager addTask(Task task) {
        task.setTaskManager(this);
        pool.submit(task, null);
        counter++;

        return this;
    }

    public void put(String id, Object o) {
//        System.out.println("PUT id = [" + id + "], o = [" + o + "]");
        context.get(id).set(o);
    }

    public Object get(String id) {
//        System.out.println("GET id = [" + id + "]");
        Object value =  context.get(id).get();
//        System.out.println("GET value = " + value);
        return value;
    }

    public void process(){
        try{
            for(int i = 0; i < counter; i++)
                pool.take().get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            counter = 0;
        }
    }

    public Map<String, Object> getContext() {
        return view;
    }


    class ValueLatchMap<K> extends HashMap<K, ValueLatch> {

        @Override
        public ValueLatch get(Object key) {
            if (containsKey(key))
                return super.get(key);

            ValueLatch latch = new ValueLatch(key);
            put((K) key, latch);
            return latch;
        }
    }

    class ValueLatch extends CountDownLatch {

        Object K;
        Object value;

        ValueLatch(Object K) {
            super(1);
            this.K = K;
        }

        void set(Object value) {
            this.value = value;
            view .put((String) K, value);
            countDown();
        }

        Object get() {
            try {
                await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return value;
        }

    }

}
