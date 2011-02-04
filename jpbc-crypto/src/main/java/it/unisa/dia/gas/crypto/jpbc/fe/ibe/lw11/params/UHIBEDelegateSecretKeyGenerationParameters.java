package it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.params;

import it.unisa.dia.gas.jpbc.Element;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UHIBEDelegateSecretKeyGenerationParameters extends KeyGenerationParameters {

    private UHIBEPublicKeyParameters publicKey;
    private UHIBESecretKeyParameters secretKey;
    private Element id;


    public UHIBEDelegateSecretKeyGenerationParameters(UHIBEPublicKeyParameters publicKey, UHIBESecretKeyParameters secretKey, Element id) {
        super(null, 12);

        this.publicKey = publicKey;
        this.secretKey = secretKey;
        this.id = id.getImmutable();
    }

    public UHIBEPublicKeyParameters getPublicKey() {
        return publicKey;
    }

    public UHIBESecretKeyParameters getSecretKey() {
        return secretKey;
    }

    public Element getId() {
        return id;
    }
}
