package it.unisa.dia.gas.plaf.jpbc.crypto.hve.params;

import org.bouncycastle.crypto.KeyGenerationParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HVEKeyGenerationParameters extends KeyGenerationParameters {

    private HVEParameters params;

    public HVEKeyGenerationParameters(SecureRandom random, HVEParameters params) {
        super(random, params.getG().getField().getLengthInBytes());

        this.params = params;
    }

    public HVEParameters getParameters() {
        return params;
    }

}