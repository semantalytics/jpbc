package it.unisa.dia.gas.plaf.jpbc.crypto.ibe.dip10.params;

import it.unisa.dia.gas.jpbc.Element;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class AHIBESecretKeyGenerationParameters extends KeyGenerationParameters {

    private AHIBEMasterSecretKeyParameters ahiveMasterSecretKeyParameters;
    private AHIBEPublicKeyParameters ahivePublicKeyParameters;
    private Element[] ids;


    public AHIBESecretKeyGenerationParameters(
            AHIBEMasterSecretKeyParameters AHIBEMasterSecretKeyParameters,
            AHIBEPublicKeyParameters AHIBEPublicKeyParameters,
            Element[] ids) {
        super(null, 12);
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
