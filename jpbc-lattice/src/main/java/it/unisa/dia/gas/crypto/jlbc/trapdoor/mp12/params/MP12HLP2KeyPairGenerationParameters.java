package it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.params;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class MP12HLP2KeyPairGenerationParameters extends MP12PLP2KeyPairGenerationParameters {

    public MP12HLP2KeyPairGenerationParameters(SecureRandom random,
                                               int n,
                                               int k,
                                               int gaussianParameter) {
        super(random, n, k, gaussianParameter);
    }

    public MP12HLP2KeyPairGenerationParameters(MP12Parameters params, int k, int gaussianParameter) {
        super(params, k, gaussianParameter);
    }

}
