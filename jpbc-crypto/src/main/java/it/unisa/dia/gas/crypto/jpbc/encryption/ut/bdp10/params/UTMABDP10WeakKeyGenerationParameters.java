package it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.params;

import org.bouncycastle.crypto.KeyGenerationParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMABDP10WeakKeyGenerationParameters extends KeyGenerationParameters {

    private UTMABDP10WeakParameters params;

    public UTMABDP10WeakKeyGenerationParameters(SecureRandom random, UTMABDP10WeakParameters params) {
        super(random, params.getPublicParameters().getG().getField().getLengthInBytes());

        this.params = params;
    }

    public UTMABDP10WeakParameters getParameters() {
        return params;
    }

}
