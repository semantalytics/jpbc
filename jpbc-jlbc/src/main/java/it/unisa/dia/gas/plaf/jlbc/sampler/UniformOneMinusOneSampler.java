package it.unisa.dia.gas.plaf.jlbc.sampler;

import it.unisa.dia.gas.plaf.jpbc.sampler.Sampler;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class UniformOneMinusOneSampler implements Sampler<BigInteger> {

    private static final BigInteger ONE = BigInteger.valueOf(1);
    private static final BigInteger MINUS_ONE = BigInteger.valueOf(-1);

    protected SecureRandom random;


    public UniformOneMinusOneSampler(SecureRandom random) {
        this.random = random;
    }

    public BigInteger sample() {
        return (random.nextBoolean()) ? ONE : MINUS_ONE;
    }

}
