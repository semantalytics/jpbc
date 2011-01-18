package it.unisa.dia.gas.jpbc;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public interface CurveParameters {

    String getString(String key);

    String getString(String key, String defaultValue);

    int getInt(String key);

    BigInteger getBigInteger(String key);

    BigInteger getBigInteger(String key, BigInteger defaultValue);

    long getLong(String key);
}
