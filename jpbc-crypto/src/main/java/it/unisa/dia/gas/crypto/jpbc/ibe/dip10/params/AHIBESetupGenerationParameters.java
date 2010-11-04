package it.unisa.dia.gas.plaf.jpbc.crypto.ibe.dip10.params;

import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class AHIBESetupGenerationParameters extends KeyGenerationParameters {

    private int bitLength;
    private int length;


    public AHIBESetupGenerationParameters(int bitLength, int length) {
        super(null, 12);
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
