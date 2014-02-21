package it.unisa.dia.gas.crypto.jpbc.fe.abe.gvw13.params;

import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class GVW13MasterSecretKeyParameters extends GVW13KeyParameters {

    private CipherParameters[] torPrivateKeys;


    public GVW13MasterSecretKeyParameters(GVW13Parameters parameters, CipherParameters[] torPrivateKeys) {
        super(true, parameters);

        this.torPrivateKeys = torPrivateKeys;
    }


    public CipherParameters getCipherParametersAt(int index, int b) {
        return torPrivateKeys[index * 2 + b];
    }

}
