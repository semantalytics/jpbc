package it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.params;

import it.unisa.dia.gas.crypto.jpbc.utils.ElementUtil;
import it.unisa.dia.gas.jpbc.Element;
import org.bouncycastle.crypto.KeyGenerationParameters;

import java.util.Arrays;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UHIBESecretKeyGenerationParameters extends KeyGenerationParameters {

    private UHIBEMasterSecretKeyParameters masterSecretKey;
    private UHIBEPublicKeyParameters publicKey;
    private Element[] ids;


    public UHIBESecretKeyGenerationParameters(
            UHIBEMasterSecretKeyParameters UHIBEMasterSecretKeyParameters,
            UHIBEPublicKeyParameters UHIBEPublicKeyParameters,
            Element[] ids) {

        super(null, 12);

        this.masterSecretKey = UHIBEMasterSecretKeyParameters;
        this.publicKey = UHIBEPublicKeyParameters;
        this.ids = ElementUtil.cloneImmutably(ids);
    }


    public UHIBEMasterSecretKeyParameters getMasterSecretKey() {
        return masterSecretKey;
    }

    public UHIBEPublicKeyParameters getPublicKey() {
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
