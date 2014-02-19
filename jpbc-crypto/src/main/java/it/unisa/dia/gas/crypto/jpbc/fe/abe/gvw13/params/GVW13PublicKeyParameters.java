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

    public CipherParameters getCipherParametersAt(int index) {
        return torPublicKeys[index];
    }
}
