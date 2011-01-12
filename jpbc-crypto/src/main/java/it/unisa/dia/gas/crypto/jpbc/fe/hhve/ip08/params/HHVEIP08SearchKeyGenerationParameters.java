package it.unisa.dia.gas.crypto.jpbc.fe.hhve.ip08.params;

import org.bouncycastle.crypto.KeyGenerationParameters;

import java.util.Arrays;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HHVEIP08SearchKeyGenerationParameters extends KeyGenerationParameters {
    private HHVEIP08PrivateKeyParameters privateKey;
    private int[] pattern;
    private boolean allStar;


    public HHVEIP08SearchKeyGenerationParameters(HHVEIP08PrivateKeyParameters privateKey, int... pattern) {
        super(null, privateKey.getParameters().getG().getField().getLengthInBytes());

        this.privateKey = privateKey;
        this.pattern = Arrays.copyOf(pattern, pattern.length);

        int numStar = 0;
        for (int i = 0; i < pattern.length; i++) {
            int patter = pattern[i];

            if (patter < 0)
                numStar++;
        }
        allStar = (numStar == pattern.length);
    }

    public HHVEIP08PrivateKeyParameters getPrivateKey() {
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
}