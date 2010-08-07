package it.unisa.dia.gas.plaf.jpbc.crypto.ahibe.params;

import it.unisa.dia.gas.jpbc.Element;
import org.bouncycastle.crypto.KeyGenerationParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class AHIBESecretKeyGenerationParameters extends KeyGenerationParameters {

    private AHIBEMasterSecretKeyParameters ahiveMasterSecretKeyParameters;
    private AHIBEPublicKeyParameters ahivePublicKeyParameters;
    private Element[] ids;


    public AHIBESecretKeyGenerationParameters(SecureRandom random, int i,
                                              AHIBEMasterSecretKeyParameters AHIBEMasterSecretKeyParameters,
                                              AHIBEPublicKeyParameters AHIBEPublicKeyParameters,
                                              Element[] ids) {
        super(random, i);
        this.ahiveMasterSecretKeyParameters = AHIBEMasterSecretKeyParameters;
        this.ahivePublicKeyParameters = AHIBEPublicKeyParameters;
        this.ids = ids;
    }


    public AHIBEMasterSecretKeyParameters getMasterSecretKey() {
        return ahiveMasterSecretKeyParameters;
    }

    public AHIBEPublicKeyParameters getPublicKey() {
        return ahivePublicKeyParameters;
    }

    public Element[] getIds() {
        return ids;
    }
}
