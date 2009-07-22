package it.unisa.dia.gas.plaf.jpbc.pairing;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Point;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public interface PairingMap {

    Element pairing(Point in1, Point in2);

    void finalPow(Element element);

    void initPairingPreProcessing(Element in1);

    Element pairing(Point in2);

}
