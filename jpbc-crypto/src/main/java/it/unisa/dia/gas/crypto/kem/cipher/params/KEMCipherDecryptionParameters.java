package it.unisa.dia.gas.crypto.kem.cipher.params;

import org.bouncycastle.crypto.CipherParameters;

import java.security.AlgorithmParameters;
import java.util.Arrays;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class KEMCipherDecryptionParameters extends KEMCipherParameters {

    private final CipherParameters kemCipherParameters;
    private final byte[] encapsulation;
    private final int cipherKeyStrength;


    public KEMCipherDecryptionParameters(final CipherParameters kemCipherParameters,
                                         final byte[] encapsulation,
                                         final int cipherKeyStrength) {
        super(null);
        this.kemCipherParameters = kemCipherParameters;
        this.encapsulation = Arrays.copyOf(encapsulation, encapsulation.length);
        this.cipherKeyStrength = cipherKeyStrength;
    }

    public KEMCipherDecryptionParameters(final AlgorithmParameters algorithmParameters,
                                         final CipherParameters kemCipherParameters,
                                         final byte[] encapsulation,
                                         final int cipherKeyStrength) {
        super(algorithmParameters);
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
