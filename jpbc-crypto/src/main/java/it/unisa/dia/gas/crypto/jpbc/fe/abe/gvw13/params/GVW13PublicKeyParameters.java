package it.unisa.dia.gas.crypto.jpbc.fe.abe.gvw13.params;

import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class GVW13PublicKeyParameters extends GVW13KeyParameters {

    private CipherParameters[] torPublicKeys;

    public GVW13PublicKeyParameters(GVW13Parameters parameters, CipherParameters[] torPublicKeys) {
        super(false, parameters);

        this.torPublicKeys = torPublicKeys;
    }

    public CipherParameters getCipherParametersAt(int index, int b) {
        return torPublicKeys[index * 2 + b];
    }

    public CipherParameters getCipherParametersAt(int index, boolean b) {
        return torPublicKeys[index * 2 + (b ? 1 : 0)];
    }

    public CipherParameters getCipherParametersOut() {
        return torPublicKeys[torPublicKeys.length - 1];
    }
}
