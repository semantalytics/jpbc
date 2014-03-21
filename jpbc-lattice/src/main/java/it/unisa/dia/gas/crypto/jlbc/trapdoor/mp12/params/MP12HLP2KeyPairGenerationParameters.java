package it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.params;

import it.unisa.dia.gas.plaf.jlbc.sampler.Sampler;
import it.unisa.dia.gas.plaf.jlbc.sampler.ZGaussianCDTSampler;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class MP12HLP2KeyPairGenerationParameters extends MP12PLP2KeyPairGenerationParameters {

    public MP12HLP2KeyPairGenerationParameters(SecureRandom random,
                                               MP12Parameters params,
                                               int k,
                                               Sampler<BigInteger> sampler) {
        super(random, params, k, sampler);
    }

    public MP12HLP2KeyPairGenerationParameters(SecureRandom random,
                                               MP12Parameters params,
                                               int k) {
        super(random, params, k, new ZGaussianCDTSampler(random, 10));
    }

    public MP12HLP2KeyPairGenerationParameters(SecureRandom random,
                                               int n,
                                               int k) {
        super(random, new MP12Parameters(random, n), k, new ZGaussianCDTSampler(random, 10));
    }

    public MP12HLP2KeyPairGenerationParameters(SecureRandom random,
                                               int n) {
        super(random, new MP12Parameters(random, n), 24, new ZGaussianCDTSampler(random, 10));
    }
}
