package it.unisa.dia.gas.jpbc;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @todo: add javadocs
 * @since 1.2.0
 */
public interface CurveParameters {

    boolean containsKey(String key);

    String getString(String key);

    String getString(String key, String defaultValue);

    int getInt(String key);

    int getInt(String key, int defaultValue);

    BigInteger getBigInteger(String key);

    BigInteger getBigInteger(String key, BigInteger defaultValue);

    long getLong(String key);

    long getLong(String key, long defaultValue);

    byte[] getBytes(String key);
}
