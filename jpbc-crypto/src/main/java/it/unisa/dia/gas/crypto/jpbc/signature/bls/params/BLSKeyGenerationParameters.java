package it.unisa.dia.gas.crypto.jpbc.signature.bls.params;

import org.bouncycastle.crypto.KeyGenerationParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class BLSKeyGenerationParameters extends KeyGenerationParameters {

    private BLSParameters params;

    public BLSKeyGenerationParameters(SecureRandom random, BLSParameters params) {
        super(random, params.getG().getField().getLengthInBytes());

        this.params = params;
    }

    public BLSParameters getParameters() {
        return params;
    }

}