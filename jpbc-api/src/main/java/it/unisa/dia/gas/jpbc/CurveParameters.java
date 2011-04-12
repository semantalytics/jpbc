package it.unisa.dia.gas.jpbc;

import java.math.BigInteger;

/**
 * Represents the set of parameters describing an elliptic curve.
 *
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.2.0
 * TODO: continue to add javadocs.
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
