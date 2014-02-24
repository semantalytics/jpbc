package it.unisa.dia.gas.plaf.jlbc.tor.gvw13.params;

import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class TORGVW13SecretKeyParameters extends TORGVW13KeyParameters {

    private CipherParameters latticePrivateKey;


    public TORGVW13SecretKeyParameters(TORGVW13Parameters parameters, CipherParameters latticePrivateKey) {
        super(true, parameters);

        this.latticePrivateKey = latticePrivateKey;
    }

    public CipherParameters getLatticePrivateKey() {
        return latticePrivateKey;
    }
}
