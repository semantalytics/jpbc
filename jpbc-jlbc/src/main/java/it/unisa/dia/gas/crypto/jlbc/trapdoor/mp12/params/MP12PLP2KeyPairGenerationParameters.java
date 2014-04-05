package it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.params;

import it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.utils.MP12P2Utils;
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
    private Sampler<BigInteger> discreteGaussianSampler;

    public MP12PLP2KeyPairGenerationParameters(MP12Parameters params, int k) {
        super(params.getRandom(), 100);

        this.params = params;
        this.k = k;
        this.discreteGaussianSampler = MP12P2Utils.getPrimitiveDiscreteGaussianSampler(params.getRandom());
    }

    public MP12PLP2KeyPairGenerationParameters(SecureRandom random, int n, int k) {
        super(random, 100);

        this.params = new MP12Parameters(random, n);
        this.k = k;
        this.discreteGaussianSampler = MP12P2Utils.getPrimitiveDiscreteGaussianSampler(params.getRandom());
    }


    public MP12Parameters getParameters() {
        return params;
    }

    public int getK() {
        return k;
    }

    public Sampler<BigInteger> getDiscreteGaussianSampler() {
        return discreteGaussianSampler;
    }
}
