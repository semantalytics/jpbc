package it.unisa.dia.gas.plaf.jpbc.crypto.commitment.ly.params;

import org.bouncycastle.crypto.KeyGenerationParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class LYqTMCKeyGenerationParameters extends KeyGenerationParameters {

    private LYqTMCParameters params;
    private int q;

    public LYqTMCKeyGenerationParameters(SecureRandom random, LYqTMCParameters params, int q) {
        super(random, params.getG().getField().getLengthInBytes());

        this.params = params;
        this.q = q;
    }

    public LYqTMCParameters getParameters() {
        return params;
    }

    public int getQ() {
        return q;
    }
}