package it.unisa.dia.gas.plaf.jlbc.tor.gvw13.params;

import it.unisa.dia.gas.crypto.cipher.ElementCipher;
import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class TORGVW13PublicKeyParameters extends TORGVW13KeyParameters {

    private CipherParameters latticePublicKey;
    private ElementCipher encoder;

    public TORGVW13PublicKeyParameters(TORGVW13Parameters parameters, CipherParameters latticePublicKey, ElementCipher encoder) {
        super(false, parameters);

        this.latticePublicKey = latticePublicKey;
        this.encoder = encoder;
    }

    public ElementCipher getEncoder() {
        return encoder;
    }
}
