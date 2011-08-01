package it.unisa.dia.gas.crypto.engines.kem;

import org.bouncycastle.crypto.CipherParameters;

import java.util.Arrays;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class KEMCipherDecryptionParameters implements CipherParameters {
    private int cipherKeyStrength;
    private CipherParameters kemCipherParameters;
    private byte[] encapsulation;


    public KEMCipherDecryptionParameters(CipherParameters kemCipherParameters, byte[] encapsulation, int cipherKeyStrength) {
        this.kemCipherParameters = kemCipherParameters;
        this.encapsulation = Arrays.copyOf(encapsulation, encapsulation.length);
        this.cipherKeyStrength = cipherKeyStrength;
    }


    public CipherParameters getKemCipherParameters() {
        return kemCipherParameters;
    }

    public byte[] getEncapsulation() {
        return Arrays.copyOf(encapsulation, encapsulation.length);
    }

    public int getCipherKeyStrength() {
        return cipherKeyStrength;
    }
}
