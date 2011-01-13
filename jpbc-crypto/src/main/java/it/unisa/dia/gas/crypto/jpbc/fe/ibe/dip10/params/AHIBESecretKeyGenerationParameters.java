package it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.params;

import it.unisa.dia.gas.crypto.jpbc.utils.ElementUtil;
import it.unisa.dia.gas.jpbc.Element;
import org.bouncycastle.crypto.KeyGenerationParameters;

import java.util.Arrays;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class AHIBESecretKeyGenerationParameters extends KeyGenerationParameters {

    private AHIBEMasterSecretKeyParameters masterSecretKey;
    private AHIBEPublicKeyParameters publicKey;
    private Element[] ids;


    public AHIBESecretKeyGenerationParameters(
            AHIBEMasterSecretKeyParameters AHIBEMasterSecretKeyParameters,
            AHIBEPublicKeyParameters AHIBEPublicKeyParameters,
            Element[] ids) {

        super(null, 12);
        this.masterSecretKey = AHIBEMasterSecretKeyParameters;
        this.publicKey = AHIBEPublicKeyParameters;
        this.ids = ElementUtil.cloneImmutably(ids);
    }


    public AHIBEMasterSecretKeyParameters getMasterSecretKey() {
        return masterSecretKey;
    }

    public AHIBEPublicKeyParameters getPublicKey() {
        return publicKey;
    }

    public Element getIdAt(int index) {
        return ids[index];
    }

    public Element[] getIds() {
        return Arrays.copyOf(ids, ids.length);
    }

    public int getLength() {
        return ids.length;
    }
}
