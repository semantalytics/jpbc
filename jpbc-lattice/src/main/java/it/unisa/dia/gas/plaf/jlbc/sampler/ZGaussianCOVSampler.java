package it.unisa.dia.gas.plaf.jlbc.sampler;

import it.unisa.dia.gas.jpbc.Matrix;
import it.unisa.dia.gas.jpbc.Vector;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class ZGaussianCOVSampler implements Sampler<Vector> {

    protected SecureRandom random;
    protected Matrix cov;
    protected Sampler<Vector> sampler;


    public ZGaussianCOVSampler(SecureRandom random, Matrix cov) {
        if (random == null)
            random = new SecureRandom();

        this.random = random;
        this.cov = cov;
        this.sampler = new CGSampler(random, 128, cov.getN());
    }


    public Vector sample() {
        Vector sample = (Vector) cov.mul(sampler.sample());

        // TODO: round sampler

        return sample;
    }
}
