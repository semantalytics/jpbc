package it.unisa.dia.gas.plaf.jpbc.pairing.a;

import it.unisa.dia.gas.jpbc.Element;

/**
* @author Angelo De Caro (angelo.decaro@gmail.com)
*/
public class PairingPreProcessingInfo {
    public int numRow = 0;
    public Element[][] coeff;

    public void addRow(Element a, Element b, Element c) {
        coeff[numRow][0] = a.duplicate();
        coeff[numRow][1] = b.duplicate();
        coeff[numRow][2] = c.duplicate();
        numRow++;
    }
}
