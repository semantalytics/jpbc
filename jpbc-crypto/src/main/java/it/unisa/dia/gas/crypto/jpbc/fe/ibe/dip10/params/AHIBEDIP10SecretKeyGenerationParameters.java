package it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.params;

import it.unisa.dia.gas.crypto.jpbc.utils.ElementUtil;
import it.unisa.dia.gas.jpbc.Element;
import org.bouncycastle.crypto.KeyGenerationParameters;

import java.util.Arrays;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class AHIBEDIP10SecretKeyGenerationParameters extends KeyGenerationParameters {

    private AHIBEDIP10MasterSecretKeyParameters masterSecretKey;
    private AHIBEDIP10PublicKeyParameters publicKey;
    private Element[] ids;


    public AHIBEDIP10SecretKeyGenerationParameters(
            AHIBEDIP10MasterSecretKeyParameters AHIBEMasterSecretKeyParameters,
            AHIBEDIP10PublicKeyParameters AHIBEPublicKeyParameters,
            Element[] ids) {

        super(null, 12);
        this.masterSecretKey = AHIBEMasterSecretKeyParameters;
        this.publicKey = AHIBEPublicKeyParameters;
        this.ids = ElementUtil.cloneImmutably(ids);
    }


    public AHIBEDIP10MasterSecretKeyParameters getMasterSecretKey() {
        return masterSecretKey;
    }

    public AHIBEDIP10PublicKeyParameters getPublicKey() {
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
