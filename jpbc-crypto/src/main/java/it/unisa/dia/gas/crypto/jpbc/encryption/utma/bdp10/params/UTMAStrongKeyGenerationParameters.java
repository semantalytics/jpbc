package it.unisa.dia.gas.crypto.jpbc.encryption.utma.bdp10.params;

import org.bouncycastle.crypto.KeyGenerationParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMAStrongKeyGenerationParameters extends KeyGenerationParameters {

    private UTMAStrongParameters params;
    

    public UTMAStrongKeyGenerationParameters(SecureRandom random, UTMAStrongParameters params) {
        super(random, params.getPublicParameters().getG().getField().getLengthInBytes());

        this.params = params;
    }

    public UTMAStrongParameters getParameters() {
        return params;
    }

}