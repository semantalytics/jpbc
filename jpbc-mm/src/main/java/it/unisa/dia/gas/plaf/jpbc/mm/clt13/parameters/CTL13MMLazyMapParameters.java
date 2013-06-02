package it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters;

import it.unisa.dia.gas.jpbc.PairingParameters;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class CTL13MMLazyMapParameters implements PairingParameters {

    public boolean containsKey(String key) {
        return false;  
    }

    public String getString(String key) {
        return null;  
    }

    public String getString(String key, String defaultValue) {
        return null;  
    }

    public int getInt(String key) {
        return 0;  
    }

    public int getInt(String key, int defaultValue) {
        return 0;  
    }

    public BigInteger getBigInteger(String key) {
        return null;  
    }

    public BigInteger getBigIntegerAt(String key, int index) {
        return null;  
    }

    public BigInteger getBigInteger(String key, BigInteger defaultValue) {
        return null;  
    }

    public long getLong(String key) {
        return 0;  
    }

    public long getLong(String key, long defaultValue) {
        return 0;  
    }

    public byte[] getBytes(String key) {
        return new byte[0];  
    }

    public byte[] getBytes(String key, byte[] defaultValue) {
        return new byte[0];  
    }

    public String toString(String separator) {
        return null;  
    }

    public Object getObject(String key) {
        return null;  
    }
}
