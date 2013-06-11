package it.unisa.dia.gas.plaf.jpbc.util.collection;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class FlagMap<K> {

    protected LatchHashMap<K, Boolean> flags;

    public FlagMap() {
        this.flags = new LatchHashMap<K, Boolean>();
    }

    public void get(K key) {
        flags.get(key);
    }

    public void set(K key) {
        flags.put(key, true);
    }

}
