package it.unisa.dia.gas.plaf.jpbc.mm.clt13.engine;

import it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters.CTL13InstanceParameters;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public interface CTL13MMInstance {

    CTL13InstanceParameters getParameters();

    BigInteger reduce(BigInteger value);

    boolean isZero(BigInteger value, int index);

    BigInteger sampleAtLevel(int index);

    BigInteger sampleAtZero();

    BigInteger encodeAt(BigInteger value, int startIndex, int endIndex);

    BigInteger encodeAt(int degree);

    BigInteger encodeZero();

    BigInteger encodeZeroAt(int index);

    BigInteger encodeOne();

    BigInteger encodeOneAt(int index);

    BigInteger reRandomize(BigInteger value, int index);

}
