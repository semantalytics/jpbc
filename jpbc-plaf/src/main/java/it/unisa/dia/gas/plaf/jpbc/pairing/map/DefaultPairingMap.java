package it.unisa.dia.gas.plaf.jpbc.pairing.map;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Point;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public abstract class DefaultPairingMap implements PairingMap {
    protected Point in1;


    public void initPairingPreProcessing(Point in1) {
        this.in1 = in1;
    }

    public Element pairing(Point in2) {
        return pairing(in1, in2);
    }
}
