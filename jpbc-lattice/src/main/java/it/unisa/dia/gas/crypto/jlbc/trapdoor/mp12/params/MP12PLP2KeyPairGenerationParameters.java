package it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.params;

import it.unisa.dia.gas.plaf.jlbc.sampler.SamplerFactory;
import it.unisa.dia.gas.plaf.jpbc.sampler.Sampler;
import org.bouncycastle.crypto.KeyGenerationParameters;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class MP12PLP2KeyPairGenerationParameters extends KeyGenerationParameters {

    private MP12Parameters params;
    private int k;
    private int gaussianParameter;
    private Sampler<BigInteger> ZSampler;

    public MP12PLP2KeyPairGenerationParameters(MP12Parameters params,
                                               int k,
                                               int gaussianParameter) {
        super(params.getRandom(), 100);

        this.params = params;
        this.k = k;
        this.gaussianParameter = gaussianParameter;
        this.ZSampler = SamplerFactory.getInstance().getDiscreteGaussianSampler(params.getRandom(), gaussianParameter);
    }

    public MP12PLP2KeyPairGenerationParameters(SecureRandom random,
                                               int n,
                                               int k,
                                               int gaussianParameter) {
        super(random, 100);

        this.params = new MP12Parameters(random, n);
        this.k = k;
        this.gaussianParameter = gaussianParameter;
        this.ZSampler = SamplerFactory.getInstance().getDiscreteGaussianSampler(params.getRandom(), gaussianParameter);
    }


    public MP12Parameters getParameters() {
        return params;
    }

    public int getK() {
        return k;
    }

    public int getGaussianParameter() {
        return gaussianParameter;
    }

    public Sampler<BigInteger> getZSampler() {
        return ZSampler;
    }
}
