package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params;

import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HVEIP08SearchKeyGenerationParameters extends KeyGenerationParameters {

    private HVEIP08PrivateKeyParameters params;
    private byte[] attributePattern;


    public HVEIP08SearchKeyGenerationParameters(HVEIP08PrivateKeyParameters params, byte[] attributePattern) {
        super(null, params.getParameters().getG().getField().getLengthInBytes());

        this.params = params;
        this.attributePattern = attributePattern;
    }

    public HVEIP08PrivateKeyParameters getParameters() {
        return params;
    }

    public byte[] getAttributePattern() {
        return attributePattern;
    }
}