package it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.params;

import org.bouncycastle.crypto.KeyGenerationParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMABDP10StrongKeyGenerationParameters extends KeyGenerationParameters {

    private UTMABDP10StrongParameters params;
    

    public UTMABDP10StrongKeyGenerationParameters(SecureRandom random, UTMABDP10StrongParameters params) {
        super(random, params.getPublicParameters().getG().getField().getLengthInBytes());

        this.params = params;
    }

    public UTMABDP10StrongParameters getParameters() {
        return params;
    }

}