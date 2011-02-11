package it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.params;

import it.unisa.dia.gas.crypto.jpbc.utils.ElementUtil;
import it.unisa.dia.gas.jpbc.Element;
import org.bouncycastle.crypto.CipherParameters;

import java.util.Arrays;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UHIBELW11EncryptionParameters implements CipherParameters {

    private UHIBELW11PublicKeyParameters publicKey;
    private Element[] ids;


    public UHIBELW11EncryptionParameters(UHIBELW11PublicKeyParameters publicKey,
                                         Element[] ids) {
        this.publicKey = publicKey;
        this.ids = ElementUtil.cloneImmutably(ids);
    }


    public UHIBELW11PublicKeyParameters getPublicKey() {
        return publicKey;
    }

    public Element[] getIds() {
        return Arrays.copyOf(ids, ids.length);
    }

    public Element getIdAt(int index) {
        return ids[index];
    }

    public int getLength() {
        return ids.length;
    }
}
