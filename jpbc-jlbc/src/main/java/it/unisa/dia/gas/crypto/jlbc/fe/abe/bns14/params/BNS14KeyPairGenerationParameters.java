package it.unisa.dia.gas.crypto.jlbc.fe.abe.bns14.params;

import org.bouncycastle.crypto.KeyGenerationParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class BNS14KeyPairGenerationParameters extends KeyGenerationParameters {

    private BNS14Parameters params;

    public BNS14KeyPairGenerationParameters(SecureRandom random, BNS14Parameters params) {
        super(random, 0);

        this.params = params;
    }

    public BNS14Parameters getParameters() {
        return params;
    }

}
