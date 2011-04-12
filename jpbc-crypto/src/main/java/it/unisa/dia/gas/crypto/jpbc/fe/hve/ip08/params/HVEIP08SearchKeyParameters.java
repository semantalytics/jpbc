package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params;

import it.unisa.dia.gas.crypto.jpbc.utils.ElementUtil;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingPreProcessing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.util.Arrays;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HVEIP08SearchKeyParameters extends HVEIP08KeyParameters {
    private int[] pattern;
    private Element[] Y, L;
    private Element K;
    private boolean allStar;

    private PairingPreProcessing[] preY, preL;
    private boolean preProcessed = false;


    public HVEIP08SearchKeyParameters(HVEIP08Parameters parameters) {
        this(parameters, null);
    }

    public HVEIP08SearchKeyParameters(HVEIP08Parameters parameters, Element k) {
        super(true, parameters);

        this.K = (k != null) ? k.getImmutable() : null;
        this.pattern = new int[parameters.getN()];
        for (int i = 0; i < pattern.length; i++)
            pattern[i] = -1;
        this.allStar = true;
    }

    public HVEIP08SearchKeyParameters(HVEIP08Parameters parameters, int[] pattern, Element[] Y, Element[] L) {
        super(true, parameters);

        this.pattern = Arrays.copyOf(pattern, pattern.length);
        this.Y = ElementUtil.cloneImmutably(Y);
        this.L = ElementUtil.cloneImmutably(L);

        this.allStar = false;
        this.K = null;
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

    public int[] getPattern() {
        return Arrays.copyOf(pattern, pattern.length);
    }

    public boolean isAllStar() {
        return allStar;
    }

    public PairingPreProcessing getPreYAt(int index) {
        return preY[index];
    }

    public PairingPreProcessing getPreLAt(int index) {
        return preL[index];
    }

    public void preProcess() {
        Pairing pairing = PairingFactory.getPairing(getParameters().getCurveParams());
        int  n = getParameters().getN();

        preY = new PairingPreProcessing[n];
        preL = new PairingPreProcessing[n];
        for (int i = 0; i < n; i++) {
            Element Y = getYAt(i);
            Element L = getLAt(i);

            preY[i] = Y != null ? pairing.pairing(Y) : null;
            preL[i] = L != null ? pairing.pairing(L) : null;

        }

        preProcessed = true;
    }


    public boolean isPreProcessed() {
        return preProcessed;
    }
}