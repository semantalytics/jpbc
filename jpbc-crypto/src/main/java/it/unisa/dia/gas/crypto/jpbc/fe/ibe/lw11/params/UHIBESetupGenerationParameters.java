package it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.params;

import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UHIBESetupGenerationParameters extends KeyGenerationParameters {

    private int bitLength;


    public UHIBESetupGenerationParameters(int bitLength) {
        super(null, 12);
        this.bitLength = bitLength;
    }
    

    public int getBitLength() {
        return bitLength;
    }

}
