package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params;

import it.unisa.dia.gas.crypto.jpbc.utils.ElementUtil;
import it.unisa.dia.gas.jpbc.Element;

import java.util.Arrays;
import java.util.List;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HHVEIP08SearchKeyParameters extends HVEIP08KeyParameters {
    private int[] pattern;
    private Element[] Y, L;
    private List<List<Element>> SY, SL;
    private Element K;
    private boolean allStar;

    public HHVEIP08SearchKeyParameters(HVEIP08Parameters parameters,
                                       int[] pattern,
                                       Element[] Y, Element[] L,
                                       List<List<Element>> SY,
                                       List<List<Element>> SL) {
        super(true, parameters);

        this.pattern = Arrays.copyOf(pattern, pattern.length);
        this.Y = ElementUtil.cloneImmutably(Y);
        this.L = ElementUtil.cloneImmutably(L);
        this.SY = SY;
        this.SL = SL;

        this.allStar = false;
        this.K = null;
    }

    public HHVEIP08SearchKeyParameters(HVEIP08Parameters parameters, Element k) {
        super(true, parameters);

        this.K = (k != null) ? k.getImmutable() : null;
        this.allStar = true;
    }

    public HHVEIP08SearchKeyParameters(HVEIP08Parameters parameters,
                                       Element[] Y, Element[] L,
                                       List<List<Element>> SY,
                                       List<List<Element>> SL) {
        super(true, parameters);

        this.Y = ElementUtil.cloneImmutably(Y);
        this.L = ElementUtil.cloneImmutably(L);
        this.SY = SY;
        this.SL = SL;

        this.pattern = new int[parameters.getN()];
        for (int i = 0; i < pattern.length; i++)
            pattern[i] = -1;
        this.allStar = true;
    }

    public HHVEIP08SearchKeyParameters(HVEIP08Parameters parameters,
                                       Element[] Y, Element[] L,
                                       List<List<Element>> SY,
                                       List<List<Element>> SL,
                                       Element k) {
        super(true, parameters);

        this.Y = ElementUtil.cloneImmutably(Y);
        this.L = ElementUtil.cloneImmutably(L);
        this.SY = SY;
        this.SL = SL;
        this.K = (k != null) ? k.getImmutable() : null;

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

    public Element getSYAt(int row, int col) {
        return SY.get(row).get(col);
    }

    public Element getSLAt(int row, int col) {
        return SL.get(row).get(col);
    }

    public boolean isAllStar() {
        return allStar;
    }
}
