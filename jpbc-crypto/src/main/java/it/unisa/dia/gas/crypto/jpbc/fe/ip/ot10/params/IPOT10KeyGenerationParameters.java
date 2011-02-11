package it.unisa.dia.gas.crypto.jpbc.fe.ip.ot10.params;

import org.bouncycastle.crypto.KeyGenerationParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class IPOT10KeyGenerationParameters extends KeyGenerationParameters {

    private IPOT10Parameters params;

    public IPOT10KeyGenerationParameters(SecureRandom random, IPOT10Parameters params) {
        super(random, params.getG().getField().getLengthInBytes());

        this.params = params;
    }

    public IPOT10Parameters getParameters() {
        return params;
    }

}