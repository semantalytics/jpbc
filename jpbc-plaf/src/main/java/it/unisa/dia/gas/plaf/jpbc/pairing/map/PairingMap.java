package it.unisa.dia.gas.plaf.jpbc.pairing.map;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.PairingPreProcessing;
import it.unisa.dia.gas.jpbc.Point;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public interface PairingMap extends PairingPreProcessing {

    Element pairing(Point in1, Point in2);

    PairingPreProcessing pairingPreProcessing(Point in1);


    void finalPow(Element element);

}
