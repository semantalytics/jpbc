package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;

import java.util.List;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HHVEIP08PublicKeyParameters extends HHVEIP08KeyParameters {
    private Element Y;
    private List<List<ElementPowPreProcessing>> T, V;

    public HHVEIP08PublicKeyParameters(HHVEIP08Parameters parameters, Element Y, List<List<ElementPowPreProcessing>> T, List<List<ElementPowPreProcessing>> V) {
        super(false, parameters);

        this.Y = Y;

        this.T = T;
        this.V = V;
    }


    public Element getY() {
        return Y;
    }

    public ElementPowPreProcessing getTAt(int row, int col) {
        return T.get(row).get(col);
    }

    public ElementPowPreProcessing getVAt(int row, int col) {
        return V.get(row).get(col);
    }
}