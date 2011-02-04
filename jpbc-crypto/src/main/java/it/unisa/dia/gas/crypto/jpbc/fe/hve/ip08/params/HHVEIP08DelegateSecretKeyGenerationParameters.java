package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params;

import org.bouncycastle.crypto.KeyGenerationParameters;

import java.util.Arrays;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HHVEIP08DelegateSecretKeyGenerationParameters extends KeyGenerationParameters {

    private HHVEIP08PublicKeyParameters publicKey;
    private HHVEIP08SearchKeyParameters searchKey;
    private int[] pattern;


    public HHVEIP08DelegateSecretKeyGenerationParameters(HHVEIP08PublicKeyParameters publicKey, HHVEIP08SearchKeyParameters searchKey, int... pattern) {
        super(null, 12);
        this.publicKey = publicKey;
        this.searchKey = searchKey;
        this.pattern = Arrays.copyOf(pattern, pattern.length);
    }


    public HHVEIP08PublicKeyParameters getPublicKey() {
        return publicKey;
    }

    public HHVEIP08SearchKeyParameters getSearchKey() {
        return searchKey;
    }

    public int getPatternAt(int index) {
        return pattern[index];
    }

    public boolean isStarAt(int index) {
        return pattern[index] < 0;
    }

    public int[] getPattern() {
        return Arrays.copyOf(pattern, pattern.length);
    }
}
