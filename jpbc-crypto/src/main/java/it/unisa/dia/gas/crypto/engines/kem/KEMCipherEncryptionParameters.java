package it.unisa.dia.gas.crypto.engines.kem;

import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class KEMCipherEncryptionParameters implements CipherParameters {
    private int cipherKeyStrength;
    private CipherParameters kemCipherParameters;


    public KEMCipherEncryptionParameters(int cipherKeyStrength, CipherParameters kemCipherParameters) {
        this.cipherKeyStrength = cipherKeyStrength;
        this.kemCipherParameters = kemCipherParameters;
    }

    public int getCipherKeyStrength() {
        return cipherKeyStrength;
    }

    public CipherParameters getKemCipherParameters() {
        return kemCipherParameters;
    }
}
