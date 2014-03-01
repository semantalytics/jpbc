package it.unisa.dia.gas.plaf.jlbc.trapdoor.mp12.params;

import org.bouncycastle.crypto.CipherParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class MP12Parameters implements CipherParameters {

    protected SecureRandom random;
    protected int n;

    public MP12Parameters(SecureRandom random, int n) {
        this.random = random;
        this.n = n;
    }

    public SecureRandom getRandom() {
        return random;
    }

    public int getN() {
        return n;
    }

}