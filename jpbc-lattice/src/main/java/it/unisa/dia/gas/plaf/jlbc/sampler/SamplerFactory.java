package it.unisa.dia.gas.plaf.jlbc.sampler;

import it.unisa.dia.gas.plaf.jlbc.util.ApfloatUtils;
import it.unisa.dia.gas.plaf.jpbc.sampler.Sampler;
import org.apfloat.Apfloat;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class SamplerFactory {

    private static final SamplerFactory INSTANCE = new SamplerFactory();

    public static SamplerFactory getInstance() {
        return INSTANCE;
    }

    private SamplerFactory() {
    }


    public Sampler<BigInteger> getDiscreteGaussianSampler(SecureRandom random, Apfloat gaussianParameter) {
        return new DiscreteGaussianCDTSampler(random, gaussianParameter);
    }

    public Sampler<BigInteger> getDiscreteGaussianSampler(SecureRandom random, int gaussianParameter) {
        return new DiscreteGaussianCDTSampler(random, ApfloatUtils.newApfloat(gaussianParameter));
    }
}
