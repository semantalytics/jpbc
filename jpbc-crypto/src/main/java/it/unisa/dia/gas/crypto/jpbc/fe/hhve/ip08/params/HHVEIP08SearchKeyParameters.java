package it.unisa.dia.gas.crypto.jpbc.fe.hhve.ip08.params;

import it.unisa.dia.gas.jpbc.Element;

import java.util.List;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HHVEIP08SearchKeyParameters extends HHVEIP08KeyParameters {
    private int[] attributePattern;
    private Element[] Y, L;
    private List<List<Element>> SY, SL;
    private Element K;


    public HHVEIP08SearchKeyParameters(HHVEIP08Parameters parameters,
                                       int[] attributePattern,
                                       Element[] Y, Element[] L,
                                       List<List<Element>> SY,
                                       List<List<Element>> SL) {
        super(true, parameters);

        this.attributePattern = attributePattern;
        this.Y = Y;
        this.L = L;
        this.SY = SY;
        this.SL = SL;
        this.K = null;
    }

    public HHVEIP08SearchKeyParameters(HHVEIP08Parameters parameters, Element k) {
        super(true, parameters);
        K = k;
    }


    public Element getK() {
        return K;
    }

    public boolean isStar(int index) {
        return attributePattern[index] < 0;
    }

    public Element getYAt(int index) {
        return Y[index];
    }

    public Element getLAt(int index) {
        return L[index];
    }
}