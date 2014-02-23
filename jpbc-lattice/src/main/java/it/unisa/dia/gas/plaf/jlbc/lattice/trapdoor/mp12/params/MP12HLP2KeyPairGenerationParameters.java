package it.unisa.dia.gas.plaf.jlbc.lattice.trapdoor.mp12.params;

import it.unisa.dia.gas.plaf.jlbc.sampler.Sampler;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class MP12HLP2KeyPairGenerationParameters extends MP12PLP2KeyPairGenerationParameters {

    public MP12HLP2KeyPairGenerationParameters(SecureRandom random, MP12Parameters params, int k, Sampler<BigInteger> sampler) {
        super(random, params, k, sampler);
    }
}
