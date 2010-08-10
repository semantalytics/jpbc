package it.unisa.dia.gas.plaf.jpbc.crypto.ahibe.params;

import it.unisa.dia.gas.jpbc.Element;
import org.bouncycastle.crypto.KeyGenerationParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class AHIBEDelegateSecretKeyGenerationParameters extends KeyGenerationParameters {

    private AHIBEPublicKeyParameters publicKey;
    private AHIBESecretKeyParameters secretKey;
    private Element id;


    public AHIBEDelegateSecretKeyGenerationParameters(SecureRandom secureRandom, int i, AHIBEPublicKeyParameters publicKey, AHIBESecretKeyParameters secretKey, Element id) {
        super(secureRandom, i);
        this.publicKey = publicKey;
        this.secretKey = secretKey;
        this.id = id;
    }

    public AHIBEPublicKeyParameters getPublicKey() {
        return publicKey;
    }

    public AHIBESecretKeyParameters getSecretKey() {
        return secretKey;
    }

    public Element getId() {
        return id;
    }
}
