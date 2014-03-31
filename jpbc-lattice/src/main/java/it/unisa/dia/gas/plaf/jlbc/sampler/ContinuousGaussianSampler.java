package it.unisa.dia.gas.plaf.jlbc.sampler;

import it.unisa.dia.gas.plaf.jpbc.sampler.Sampler;
import org.apfloat.Apfloat;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class ContinuousGaussianSampler implements Sampler<Apfloat> {

    protected SecureRandom random;
    protected int precision;


    public ContinuousGaussianSampler(int precision) {
        this(new SecureRandom(), precision);
    }

    public ContinuousGaussianSampler() {
        this(new SecureRandom(), 128);
    }

    public ContinuousGaussianSampler(SecureRandom random) {
        this(random, 128);
    }

    public ContinuousGaussianSampler(SecureRandom random, int precision) {
        if (random == null)
            random = new SecureRandom();

        this.random = random;
        this.precision = precision;
    }


    public Apfloat sample() {
        return new Apfloat(Double.toString(random.nextGaussian()), precision, 2);
    }

}
