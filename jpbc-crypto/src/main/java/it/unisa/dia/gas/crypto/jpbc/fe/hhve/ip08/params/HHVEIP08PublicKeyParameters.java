package it.unisa.dia.gas.crypto.jpbc.fe.hhve.ip08.params;

import it.unisa.dia.gas.jpbc.Element;

import java.util.List;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HHVEIP08PublicKeyParameters extends HHVEIP08KeyParameters {
    private Element Y;
    private List<List<Element>> T, V;


    public HHVEIP08PublicKeyParameters(HHVEIP08Parameters parameters, Element Y, List<List<Element>> T, List<List<Element>> V) {
        super(false, parameters);

        this.Y = Y;

        this.T = T;
        this.V = V;
    }


    public Element getY() {
        return Y;
    }

    public Element getTAt(int row, int col) {
        return T.get(row).get(col);
    }

    public Element getVAt(int row, int col) {
        return V.get(row).get(col);
    }
}