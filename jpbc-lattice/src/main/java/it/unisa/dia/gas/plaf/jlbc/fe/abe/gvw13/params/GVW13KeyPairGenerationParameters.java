package it.unisa.dia.gas.plaf.jlbc.fe.abe.gvw13.params;

import org.bouncycastle.crypto.KeyGenerationParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class GVW13KeyPairGenerationParameters extends KeyGenerationParameters {

    private GVW13Parameters params;

    public GVW13KeyPairGenerationParameters(SecureRandom random, GVW13Parameters params) {
        super(random, 0);

        this.params = params;
    }

    public GVW13Parameters getParameters() {
        return params;
    }

}
