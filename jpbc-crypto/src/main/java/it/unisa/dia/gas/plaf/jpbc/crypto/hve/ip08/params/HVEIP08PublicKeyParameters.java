package it.unisa.dia.gas.plaf.jpbc.crypto.hve.ip08.params;

import it.unisa.dia.gas.jpbc.Element;

import java.util.List;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HVEIP08PublicKeyParameters extends HVEIP08KeyParameters {
    private Element Y;
    private List<List<Element>> T, V;


    public HVEIP08PublicKeyParameters(HVEIP08Parameters parameters, Element Y, List<List<Element>> T, List<List<Element>> V) {
        super(false, parameters);

        this.Y = Y;

        this.T = T;
        this.V = V;
    }


    public Element getY() {
        return Y;
    }

    public List<List<Element>> getT() {
        return T;
    }

    public List<List<Element>> getV() {
        return V;
    }
}