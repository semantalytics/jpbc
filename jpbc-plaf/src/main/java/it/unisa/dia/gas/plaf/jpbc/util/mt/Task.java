package it.unisa.dia.gas.plaf.jpbc.util.mt;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public abstract class Task implements Runnable {

    private TaskManager taskManager;
    private String name;


    protected Task() {
    }

    protected Task(String name) {
        this.name = name;
    }


    public void put(String id, Object o) {
        taskManager.put(id, o);
    }

    public Object get(String id) {
        return taskManager.get(id);
    }

    public BigInteger getBigInteger(String id) {
        return (BigInteger) taskManager.get(id);
    }

    public BigInteger[] getBigIntegers(String id) {
        return (BigInteger[]) taskManager.get(id);
    }


    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }


    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                '}';
    }
}
