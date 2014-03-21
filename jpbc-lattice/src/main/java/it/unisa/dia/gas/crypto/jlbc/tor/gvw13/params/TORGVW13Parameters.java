package it.unisa.dia.gas.crypto.jlbc.tor.gvw13.params;

import org.bouncycastle.crypto.CipherParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class TORGVW13Parameters implements CipherParameters {

    protected SecureRandom random;
    protected int n;
    protected int depth;


    public TORGVW13Parameters(SecureRandom random, int n, int depth) {
        this.random = random;
        this.n = n;
        this.depth = depth;
    }

    public SecureRandom getRandom() {
        return random;
    }

    public int getN() {
        return n;
    }

    public int getDepth() {
        return depth;
    }
}