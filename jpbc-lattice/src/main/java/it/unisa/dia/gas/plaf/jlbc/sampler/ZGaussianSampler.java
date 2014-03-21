package it.unisa.dia.gas.plaf.jlbc.sampler;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class ZGaussianSampler implements Sampler<BigInteger> {

    protected SecureRandom random;

    private int gaussianParameter;
    private double sigmaSquare;

    private BigInteger tofk, left, right;

    public ZGaussianSampler(SecureRandom random, int gaussianParameter) {
        if (random == null)
            random = new SecureRandom();

        this.random = random;
        this.gaussianParameter = gaussianParameter;
        this.sigmaSquare = Math.pow(gaussianParameter, 2);
        this.tofk = BigInteger.valueOf(Math.round(Math.log(90) / Math.log(2)));

        BigInteger offset = tofk.multiply(BigInteger.valueOf(gaussianParameter));
        left = offset.negate();
        right = offset;

    }


    public BigInteger sample() {
        while (true) {
            BigInteger x = left.add(BigInteger.valueOf((long) (random.nextDouble() * (right.subtract(left)).longValue())));

            double rhoS = Math.exp(-Math.PI * Math.pow(x.longValue(), 2) / sigmaSquare);
            if (random.nextDouble() < rhoS)
                return x;
        }
    }
}
