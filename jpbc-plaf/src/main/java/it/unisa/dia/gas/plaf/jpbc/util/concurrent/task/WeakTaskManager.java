package it.unisa.dia.gas.plaf.jpbc.util.concurrent.task;

import java.lang.ref.SoftReference;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.0.0
 */
public class WeakTaskManager extends TaskManager {

    public void put(String id, Object o) {
//        System.out.println("PUT id = [" + id + "], o = [" + o + "]");
        context.get(id).set(new SoftReference<Object>(o));
    }

    @Override
    public Object get(String id) {
        SoftReference<Object> o = (SoftReference<Object>) super.get(id);
        if (o.get() == null) {
            // reload object


        }
        throw new IllegalStateException();
    }
}
