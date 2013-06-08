package it.unisa.dia.gas.plaf.jpbc.util.collection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class LatchHashMap<K, V> implements Map<K, V> {

    private Map<K, ValueLatch> internalMap;

    public LatchHashMap() {
        this.internalMap = new HashMap<K, ValueLatch>();
    }


    public int size() {
        return internalMap.size();
    }

    public boolean isEmpty() {
        return internalMap.isEmpty();
    }

    public synchronized boolean containsKey(Object key) {
        return internalMap.containsKey(key);
    }

    public boolean containsValue(Object value) {
        throw new IllegalStateException("Not implemented yet!");
    }

    public V get(Object key) {
        System.out.println("L GET key = [" + key + "]");

        return getLatch(key).get();
    }

    public V put(K key, V value) {
        System.out.println("L PUT key = [" + key + "]");

        return getLatch(key).set(value);
    }

    public V remove(Object key) {
        throw new IllegalStateException("Not implemented yet!");
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        throw new IllegalStateException("Not implemented yet!");
    }

    public void clear() {
        internalMap.clear();
    }

    public Set<K> keySet() {
        return internalMap.keySet();
    }

    public Collection<V> values() {
        throw new IllegalStateException("Not implemented yet!");
    }

    public Set<Entry<K,V>> entrySet() {
        throw new IllegalStateException("Not implemented yet!");
    }

    @Override
    public boolean equals(Object o) {
        return internalMap.equals(o);
    }

    @Override
    public int hashCode() {
        return internalMap.hashCode();
    }


    protected ValueLatch<V> getLatch(Object key) {
        ValueLatch<V> latch;
        synchronized (this) {
            if (containsKey(key))
                latch = internalMap.get(key);
            else {
                latch = new ValueLatch(key);
                internalMap.put((K) key, latch);
            }
        }
        return latch;
    }


    class ValueLatch<V> extends CountDownLatch {

        Object K;
        V value;

        ValueLatch(Object K) {
            super(1);
            this.K = K;
        }

        V set(V value) {
            V old = this.value;
            this.value = value;

            countDown();

            return old;
        }

        V get() {
            try {
                await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return value;
        }
    }

}
