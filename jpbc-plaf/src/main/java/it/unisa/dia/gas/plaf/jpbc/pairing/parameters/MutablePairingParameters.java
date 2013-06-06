package it.unisa.dia.gas.plaf.jpbc.pairing.parameters;

import it.unisa.dia.gas.jpbc.PairingParameters;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public interface MutablePairingParameters extends PairingParameters {

    void putObject(String key, Object value);

    void putBigIntegerAt(String key, int index, BigInteger value);

    void putBigInteger(String key, BigInteger value);

    void putBoolean(String key, boolean value);

}
