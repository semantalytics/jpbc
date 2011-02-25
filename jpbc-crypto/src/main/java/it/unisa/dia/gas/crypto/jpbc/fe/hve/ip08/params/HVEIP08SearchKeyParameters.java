package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params;

import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HVEIP08SearchKeyParameters extends HVEIP08KeyParameters {
    private int[] pattern;
    private Element[] Y, L;
    private Element K;
    private boolean allStar;

    public HVEIP08SearchKeyParameters(HVEIP08Parameters parameters,
                                       int[] pattern,
                                       Element[] Y, Element[] L) {
        super(true, parameters);

        this.pattern = pattern;
        this.Y = Y;
        this.L = L;

        this.allStar = false;
        this.K = null;
    }

    public HVEIP08SearchKeyParameters(HVEIP08Parameters parameters, Element k) {
        super(true, parameters);

        this.K = k;
        this.allStar = true;
    }

    public HVEIP08SearchKeyParameters(HVEIP08Parameters parameters,
                                       Element[] Y, Element[] L) {
        super(true, parameters);

        this.Y = Y;
        this.L = L;

        this.pattern = new int[parameters.getN()];
        for (int i = 0; i < pattern.length; i++)
            pattern[i] = -1;
        this.allStar = true;
    }

    public Element getK() {
        return K;
    }

    public boolean isStar(int index) {
        return pattern[index] < 0;
    }

    public Element getYAt(int index) {
        return Y[index];
    }

    public Element getLAt(int index) {
        return L[index];
    }

    public int getPatternAt(int index) {
        return pattern[index];
    }

    public boolean isAllStar() {
        return allStar;
    }
}