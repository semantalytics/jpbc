package it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.params;

import org.bouncycastle.crypto.KeyGenerationParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class GGHSW13KeyPairGenerationParameters extends KeyGenerationParameters {

    private final GGHSW13Parameters params;

    public GGHSW13KeyPairGenerationParameters(final SecureRandom random, final GGHSW13Parameters params) {
        super(random, 0);

        this.params = params;
    }

    public GGHSW13Parameters getParameters() {
        return params;
    }

}
