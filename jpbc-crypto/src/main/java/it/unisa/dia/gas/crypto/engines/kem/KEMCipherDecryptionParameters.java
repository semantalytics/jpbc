package it.unisa.dia.gas.crypto.engines.kem;

import org.bouncycastle.crypto.CipherParameters;

import java.util.Arrays;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class KEMCipherDecryptionParameters implements CipherParameters {
    private CipherParameters cipherParameters;
    private byte[] encapsulation;


    public KEMCipherDecryptionParameters(CipherParameters cipherParameters, byte[] encapsulation) {
        this.cipherParameters = cipherParameters;
        this.encapsulation = encapsulation;
    }


    public CipherParameters getCipherParameters() {
        return cipherParameters;
    }

    public byte[] getEncapsulation() {
        return Arrays.copyOf(encapsulation, encapsulation.length);
    }
}
