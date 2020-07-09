package it.unisa.dia.gas.crypto.kem.cipher.params;

import org.bouncycastle.crypto.CipherParameters;

import java.security.AlgorithmParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class KEMCipherParameters implements CipherParameters {

    private final AlgorithmParameters algorithmParameters;


    public KEMCipherParameters(final AlgorithmParameters algorithmParameters) {
        this.algorithmParameters = algorithmParameters;
    }

    public AlgorithmParameters getAlgorithmParameters() {
        return algorithmParameters;
    }
}
