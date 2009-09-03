package it.unisa.dia.gas.plaf.jpbc.pairing;

import it.unisa.dia.gas.jpbc.*;
import it.unisa.dia.gas.plaf.jpbc.pairing.map.PairingMap;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public abstract class AbstractPairing implements Pairing {

    protected Field<? extends Point> G1, G2;
    protected Field GT, Zr;

    protected PairingMap pairingMap;


    public boolean isSymmetric() {
        return true;
    }

    public Field<? extends Point> getG1() {
        return G1;
    }

    public Field<? extends Point> getG2() {
        return G2;
    }

    public Field getZr() {
        return Zr;
    }

    public Field getGT() {
        return GT;
    }

    public Element pairing(Element in1, Element in2) {
        if (!G1.equals(in1.getField()))
            throw new IllegalArgumentException("pairing 1st input mismatch");
        if (!G2.equals(in2.getField()))
            throw new IllegalArgumentException("pairing 2nd input mismatch");

        if (in1.isZero() || in2.isZero())
            return GT.newElement().setToZero();

        return pairingMap.pairing((Point) in1, (Point) in2);
    }

    public PairingPreProcessing pairing(Element in1) {
        if (!G1.equals(in1.getField()))
            throw new IllegalArgumentException("pairing 1st input mismatch");

        return pairingMap.pairingPreProcessing((Point) in1);
    }

    public boolean isAlmostCoddh(Element a, Element b, Element c, Element d) {
        return pairingMap.isAlmostCoddh(a, b, c, d);
    }


    public PairingMap getPairingMap() {
        return pairingMap;
    }

    public void setPairingMap(PairingMap pairingMap) {
        this.pairingMap = pairingMap;
    }
}
