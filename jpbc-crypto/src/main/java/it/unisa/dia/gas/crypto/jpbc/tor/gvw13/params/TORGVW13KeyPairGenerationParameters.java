package it.unisa.dia.gas.crypto.jpbc.tor.gvw13.params;

import org.bouncycastle.crypto.KeyGenerationParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class TORGVW13KeyPairGenerationParameters extends KeyGenerationParameters {

    private TORGVW13Parameters params;
    private int level;

    public TORGVW13KeyPairGenerationParameters(SecureRandom random, TORGVW13Parameters params, int level) {
        super(random, params.getG1().getField().getLengthInBytes());

        this.params = params;
        this.level = level;
    }

    public TORGVW13Parameters getParameters() {
        return params;
    }


    public int getLevel() {
        return level;
    }
}
