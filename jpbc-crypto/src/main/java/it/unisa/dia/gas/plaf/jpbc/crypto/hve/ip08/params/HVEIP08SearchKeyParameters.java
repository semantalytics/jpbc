package it.unisa.dia.gas.plaf.jpbc.crypto.hve.ip08.params;

import it.unisa.dia.gas.jpbc.Element;

import java.util.List;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HVEIP08SearchKeyParameters extends HVEIP08KeyParameters {
    private List<Element> Y, L;
    private Element K;


    public HVEIP08SearchKeyParameters(HVEIP08Parameters parameters, List<Element> Y, List<Element> L) {
        super(true, parameters);

        this.Y = Y;
        this.L = L;
        this.K = null;
    }

    public HVEIP08SearchKeyParameters(HVEIP08Parameters parameters, Element k) {
        super(true, parameters);
        K = k;
    }


    public List<Element> getY() {
        return Y;
    }

    public List<Element> getL() {
        return L;
    }

    public Element getK() {
        return K;
    }
}