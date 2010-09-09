package it.unisa.dia.gas.plaf.jpbc.crypto.hveip08.params;

import it.unisa.dia.gas.jpbc.Element;

import java.util.List;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HVEIP08PrivateKeyParameters extends HVEIP08KeyParameters {
    private Element y;
    private List<List<Element>> t,v;


    public HVEIP08PrivateKeyParameters(HVEIP08Parameters parameters, Element y, List<List<Element>> t, List<List<Element>> v) {
        super(true, parameters);

        this.y = y;

        this.t = t;
        this.v = v;
    }


    public Element getY() {
        return y;
    }

    public List<List<Element>> getT() {
        return t;
    }

    public List<List<Element>> getV() {
        return v;
    }
}