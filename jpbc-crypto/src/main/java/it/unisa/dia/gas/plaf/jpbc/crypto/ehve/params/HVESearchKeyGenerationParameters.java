package it.unisa.dia.gas.plaf.jpbc.crypto.ehve.params;

import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HVESearchKeyGenerationParameters extends KeyGenerationParameters {

    private HVEPrivateKeyParameters params;
    private byte[] attributePattern;


    public HVESearchKeyGenerationParameters(HVEPrivateKeyParameters params, byte[] attributePattern) {
        super(null, params.getParameters().getG().getField().getLengthInBytes());

        this.params = params;
        this.attributePattern = attributePattern;
    }

    public HVEPrivateKeyParameters getParameters() {
        return params;
    }

    public byte[] getAttributePattern() {
        return attributePattern;
    }
}