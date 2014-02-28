package it.unisa.dia.gas.plaf.jlbc.tor.gvw13.params;

import it.unisa.dia.gas.jpbc.Element;
import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class TORGVW13EncryptParameters implements CipherParameters {

    private TORGVW13PublicKeyParameters publicKeyParameters;
    private Element key;


    public TORGVW13EncryptParameters(TORGVW13PublicKeyParameters publicKeyParameters, Element key) {
        this.publicKeyParameters = publicKeyParameters;
        this.key = key;
    }


    public TORGVW13PublicKeyParameters getPublicKeyParameters() {
        return publicKeyParameters;
    }

    public Element getKey() {
        return key;
    }
}
