package it.unisa.dia.gas.crypto.jpbc.fe.abe.gghvv13.params;

import org.bouncycastle.crypto.KeyGenerationParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class GGHVV13KeyPairGenerationParameters extends KeyGenerationParameters {

    private final GGHVV13Parameters params;

    public GGHVV13KeyPairGenerationParameters(final SecureRandom random, final GGHVV13Parameters params) {
        super(random, 0);

        this.params = params;
    }

    public GGHVV13Parameters getParameters() {
        return params;
    }

}
