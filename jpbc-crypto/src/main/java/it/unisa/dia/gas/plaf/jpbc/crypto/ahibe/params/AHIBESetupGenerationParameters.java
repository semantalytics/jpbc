package it.unisa.dia.gas.plaf.jpbc.crypto.ahibe.params;

import org.bouncycastle.crypto.KeyGenerationParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class AHIBESetupGenerationParameters extends KeyGenerationParameters {

    private int bitLength;
    private int length;


    public AHIBESetupGenerationParameters(SecureRandom random, int i, int bitLength, int length) {
        super(random, i);
        this.bitLength = bitLength;
        this.length = length;
    }
    

    public int getBitLength() {
        return bitLength;
    }

    public int getLength() {
        return length;
    }
}
