package it.unisa.dia.gas.crypto.jpbc.fe.abe.gvw13.params;

import it.unisa.dia.gas.jpbc.Pairing;
import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class GVW13Parameters implements CipherParameters {

    private Pairing pairing;
    private int n;

    public GVW13Parameters(Pairing pairing, int n) {
        this.pairing = pairing;
        this.n = n;
    }

    public Pairing getPairing() {
        return pairing;
    }

    public int getN() {
        return n;
    }
}