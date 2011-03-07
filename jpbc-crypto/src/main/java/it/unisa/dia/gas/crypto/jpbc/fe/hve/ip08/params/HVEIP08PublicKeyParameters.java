package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPow;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @todo move to ElementPowPreProcessing
 */
public class HVEIP08PublicKeyParameters extends HVEIP08KeyParameters {
    private Element Y;
    private List<List<Element>> T, V;

    private List<List<ElementPowPreProcessing>> preT, preV;
    private boolean preProcessed = false;

    public HVEIP08PublicKeyParameters(HVEIP08Parameters parameters,
                                      Element Y,
                                      List<List<Element>> T,
                                      List<List<Element>> V) {
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

    public ElementPow getElementPowTAt(int row, int col) {
        return preProcessed ? preT.get(row).get(col) : T.get(row).get(col);
    }

    public ElementPow getElementPowVAt(int row, int col) {
        return preProcessed ? preV.get(row).get(col) : V.get(row).get(col);
    }

    public void preProcess() {
        getParameters().preProcess();

        int  n = getParameters().getN();
        preT = new ArrayList<List<ElementPowPreProcessing>>(n);
        preV = new ArrayList<List<ElementPowPreProcessing>>(n);
        for (int i = 0; i < n; i++) {
            int attributeNum = getParameters().getAttributeNumAt(i);

            List<ElementPowPreProcessing> listT = new ArrayList<ElementPowPreProcessing>(attributeNum);
            List<ElementPowPreProcessing> listV = new ArrayList<ElementPowPreProcessing>(attributeNum);
            for (int j = 0; j < attributeNum; j++) {
                listT.add(getTAt(i, j).pow());
                listV.add(getVAt(i, j).pow());
            }

            preT.add(listT);
            preV.add(listV);
        }
        preProcessed = true;
    }


    public boolean isPreProcessed() {
        return preProcessed;
    }
}