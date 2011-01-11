package it.unisa.dia.gas.crypto.jpbc.fe.hhve.ip08.params;

import org.bouncycastle.crypto.KeyGenerationParameters;

import java.util.Arrays;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HHVEIP08SearchKeyGenerationParameters extends KeyGenerationParameters {

    private HHVEIP08PrivateKeyParameters params;
    private int[] attributesPattern;
    private boolean allStar;


    public HHVEIP08SearchKeyGenerationParameters(HHVEIP08PrivateKeyParameters params, int... attributesPattern) {
        super(null, params.getParameters().getG().getField().getLengthInBytes());

        this.params = params;
        this.attributesPattern = Arrays.copyOf(attributesPattern, attributesPattern.length);

        int numStar = 0;
        for (int i = 0; i < attributesPattern.length; i++) {
            int patter = attributesPattern[i];

            if (patter < 0)
                numStar++;
        }
        allStar = (numStar == attributesPattern.length);
    }


    public HHVEIP08PrivateKeyParameters getParameters() {
        return params;
    }

    public int[] getAttributesPattern() {
        return Arrays.copyOf(attributesPattern, attributesPattern.length);
    }

    public boolean isAllStar() {
        return allStar;
    }

    public boolean isStarAt(int index) {
        return attributesPattern[index] < 0;
    }

    public int getPatternAt(int index) {
        return attributesPattern[index];
    }
}