package it.unisa.dia.gas.crypto.jpbc.fe.hhve.ip08.params;

import org.bouncycastle.crypto.KeyGenerationParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HHVEIP08KeyGenerationParameters extends KeyGenerationParameters {

    private HHVEIP08Parameters params;

    public HHVEIP08KeyGenerationParameters(SecureRandom random, HHVEIP08Parameters params) {
        super(random, params.getG().getField().getLengthInBytes());

        this.params = params;
    }

    public HHVEIP08Parameters getParameters() {
        return params;
    }

}