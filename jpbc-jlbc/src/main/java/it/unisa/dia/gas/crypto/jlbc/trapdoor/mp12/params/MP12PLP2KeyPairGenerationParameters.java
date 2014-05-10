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
    private int extraM;

    public MP12PLP2KeyPairGenerationParameters(SecureRandom random, int n, int k) {
        super(random, 100);

        this.params = new MP12Parameters(random, n);
        this.k = k;
        this.discreteGaussianSampler = MP12P2Utils.getPrimitiveDiscreteGaussianSampler(params.getRandom());
        this.extraM = 0;
    }

    public MP12PLP2KeyPairGenerationParameters(SecureRandom random, int n, int k, int extraM) {
        super(random, 100);

        this.params = new MP12Parameters(random, n);
        this.k = k;
        this.discreteGaussianSampler = MP12P2Utils.getPrimitiveDiscreteGaussianSampler(params.getRandom());
        this.extraM = extraM;
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

    public int getExtraM() {
        return extraM;
    }
}
