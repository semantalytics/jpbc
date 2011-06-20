package it.unisa.dia.gas.crypto.jpbc.rfid.utma.strong.params;

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