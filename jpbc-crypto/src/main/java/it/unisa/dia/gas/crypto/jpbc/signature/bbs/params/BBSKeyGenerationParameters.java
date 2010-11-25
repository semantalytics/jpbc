package it.unisa.dia.gas.crypto.jpbc.signature.bbs.params;

import org.bouncycastle.crypto.KeyGenerationParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class BBSKeyGenerationParameters extends KeyGenerationParameters {

    private BBSParameters params;
    private int n;


    public BBSKeyGenerationParameters(SecureRandom random, int i, BBSParameters params, int n) {
        super(random, i);
        this.params = params;
        this.n = n;
    }

    public BBSParameters getParameters() {
        return params;
    }

    public int getN() {
        return n;
    }
}