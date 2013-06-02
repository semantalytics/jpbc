package it.unisa.dia.gas.plaf.jpbc.pairing.parameters;

import it.unisa.dia.gas.jpbc.PairingParameters;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class MapParameters implements PairingParameters {

    protected LinkedHashMap<String, Object> parameters;


    public MapParameters() {
        this.parameters = new LinkedHashMap<String, Object>();
    }


    public String getType() {
        return (String) parameters.get("type");
    }

    public boolean containsKey(String key) {
        return parameters.containsKey(key);
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
        return (BigInteger) parameters.get(key);
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
        return parameters.get(key);
    }

    public String toString(String separator) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MapParameters that = (MapParameters) o;

        if (parameters != null ? !parameters.equals(that.parameters) : that.parameters != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return parameters != null ? parameters.hashCode() : 0;
    }


    public void putAll(Map<? extends String, ?> m) {
        parameters.putAll(m);
    }

    public void put(String key, Object o) {
        parameters.put(key, o);
    }

}
