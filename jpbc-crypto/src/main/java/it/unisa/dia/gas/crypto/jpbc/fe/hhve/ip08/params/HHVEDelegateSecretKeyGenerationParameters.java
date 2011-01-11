package it.unisa.dia.gas.crypto.jpbc.fe.hhve.ip08.params;

import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HHVEDelegateSecretKeyGenerationParameters extends KeyGenerationParameters {

    private HHVEIP08PublicKeyParameters publicKey;
    private HHVEIP08SearchKeyParameters secretKey;
    private byte[] attributePattern;


    public HHVEDelegateSecretKeyGenerationParameters(HHVEIP08PublicKeyParameters publicKey, HHVEIP08SearchKeyParameters secretKey, byte[] attributePattern) {
        super(null, 12);
        this.publicKey = publicKey;
        this.secretKey = secretKey;
        this.attributePattern = attributePattern;
    }


    public HHVEIP08PublicKeyParameters getPublicKey() {
        return publicKey;
    }

    public HHVEIP08SearchKeyParameters getSecretKey() {
        return secretKey;
    }

    public byte[] getAttributePattern() {
        return attributePattern;
    }
}
