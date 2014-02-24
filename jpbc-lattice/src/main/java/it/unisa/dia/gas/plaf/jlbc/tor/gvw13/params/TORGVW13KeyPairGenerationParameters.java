package it.unisa.dia.gas.plaf.jlbc.tor.gvw13.params;

import org.bouncycastle.crypto.KeyGenerationParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class TORGVW13KeyPairGenerationParameters extends KeyGenerationParameters {

    private TORGVW13Parameters parameters;


    public TORGVW13KeyPairGenerationParameters(SecureRandom random, int strength, TORGVW13Parameters parameters) {
        super(random, strength);

        this.parameters = parameters;
    }

    public TORGVW13Parameters getParameters() {
        return parameters;
    }

}
