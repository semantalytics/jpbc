package it.unisa.dia.gas.crypto.jpbc.fe.hhve.ip08.params;

import it.unisa.dia.gas.jpbc.Element;

import java.util.List;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HHVEIP08PrivateKeyParameters extends HHVEIP08KeyParameters {
    private Element y;
    private List<List<Element>> t,v;


    public HHVEIP08PrivateKeyParameters(HHVEIP08Parameters parameters, Element y, List<List<Element>> t, List<List<Element>> v) {
        super(true, parameters);

        this.y = y;

        this.t = t;
        this.v = v;
    }


    public Element getY() {
        return y;
    }

    public Element getTAt(int row, int col) {
        return t.get(row).get(col);
    }

    public Element getVAt(int row, int col) {
        return v.get(row).get(col);
    }

}