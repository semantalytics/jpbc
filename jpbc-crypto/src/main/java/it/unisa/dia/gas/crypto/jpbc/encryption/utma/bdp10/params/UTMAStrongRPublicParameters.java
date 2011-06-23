package it.unisa.dia.gas.crypto.jpbc.encryption.utma.bdp10.params;

import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMAStrongRPublicParameters implements CipherParameters {
    private CipherParameters rPrivateKey;


    public UTMAStrongRPublicParameters(CipherParameters rPrivateKey) {
        this.rPrivateKey = rPrivateKey;
    }


    public CipherParameters getRPrivateKey() {
        return rPrivateKey;
    }
}