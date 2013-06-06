package it.unisa.dia.gas.plaf.jpbc.pairing.parameters;

import it.unisa.dia.gas.jpbc.PairingParameters;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class MapParameters implements PairingParameters {

    protected Map<String, Object> values;


    public MapParameters() {
        this.values = new LinkedHashMap<String, Object>();
    }


    public String getType() {
        return (String) values.get("type");
    }

    public boolean containsKey(String key) {
        return values.containsKey(key);
    }

    public int getInt(String key) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public int getInt(String key, int defaultValue) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public long getLong(String key) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public long getLong(String key, long defaultValue) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public BigInteger getBigInteger(String key) {
        return (BigInteger) values.get(key);
    }

    public BigInteger getBigIntegerAt(String key, int index) {
        return ((BigInteger[]) getObject(key))[index];
    }

    public BigInteger getBigInteger(String key, BigInteger defaultValue) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public String getString(String key) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public String getString(String key, String defaultValue) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public byte[] getBytes(String key) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public byte[] getBytes(String key, byte[] defaultValue) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public Object getObject(String key) {
        return values.get(key);
    }

    public String toString(String separator) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MapParameters that = (MapParameters) o;

        if (values != null ? !values.equals(that.values) : that.values != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return values != null ? values.hashCode() : 0;
    }


    public void putAll(Map<? extends String, ?> m) {
        values.putAll(m);
    }

    public void put(String key, Object o) {
        values.put(key, o);
    }

}
