package it.unisa.dia.gas.plaf.jlbc.trapdoor.mp12.params;

import it.unisa.dia.gas.plaf.jlbc.sampler.Sampler;
import org.bouncycastle.crypto.KeyGenerationParameters;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class MP12PLP2KeyPairGenerationParameters extends KeyGenerationParameters {

    private MP12Parameters params;
    private int k;
    private Sampler<BigInteger> sampler;

    public MP12PLP2KeyPairGenerationParameters(SecureRandom random, MP12Parameters params, int k, Sampler<BigInteger> sampler) {
        super(random, 0);

        this.params = params;
        this.k = k;
        this.sampler =sampler;
    }

    public MP12Parameters getParameters() {
        return params;
    }

    public int getK() {
        return k;
    }

    public Sampler<BigInteger> getSampler() {
        return sampler;
    }
}
