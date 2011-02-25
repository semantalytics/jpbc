package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params;

import org.bouncycastle.crypto.KeyGenerationParameters;

import java.util.Arrays;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HVEIP08SearchKeyGenerationParameters extends KeyGenerationParameters {
    private HVEIP08PrivateKeyParameters privateKey;
    private int[] pattern;
    private boolean allStar;
    private int numNonStar;


    public HVEIP08SearchKeyGenerationParameters(HVEIP08PrivateKeyParameters privateKey, int... pattern) {
        super(null, privateKey.getParameters().getG().getField().getLengthInBytes());

        this.privateKey = privateKey;
        this.pattern = Arrays.copyOf(pattern, pattern.length);

        int numStar = 0;
        for (int i = 0; i < pattern.length; i++) {
            int patter = pattern[i];

            if (patter < 0)
                numStar++;
        }
        this.numNonStar = pattern.length - numStar;
        this.allStar = (numStar == pattern.length);
    }

    public HVEIP08PrivateKeyParameters getPrivateKey() {
        return privateKey;
    }

    public int[] getPattern() {
        return Arrays.copyOf(pattern, pattern.length);
    }

    public boolean isAllStar() {
        return allStar;
    }

    public boolean isStarAt(int index) {
        return pattern[index] < 0;
    }

    public int getPatternAt(int index) {
        return pattern[index];
    }

    public int getNumNonStar() {
        return numNonStar;
    }
}