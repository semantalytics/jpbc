package it.unisa.dia.gas.plaf.jpbc.pairing;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.Point;
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

    public Element pairing(Element g1, Element g2) {
        if (!G1.equals(g1.getField()))
            throw new IllegalArgumentException("pairing 1st input mismatch");
        if (!G2.equals(g2.getField()))
            throw new IllegalArgumentException("pairing 2nd input mismatch");

        if (g1.isZero() || g2.isZero())
            return GT.newElement().setToZero();

        return pairingMap.pairing((Point) g1, (Point) g2);
    }

    public void initPairingPreProcessing(Element g1) {
        if (!G1.equals(g1.getField()))
            throw new IllegalArgumentException("pairing 1st input mismatch");

        pairingMap.initPairingPreProcessing((Point) g1);
    }

    public Element pairing(Element g2) {
        if (!G2.equals(g2.getField()))
            throw new IllegalArgumentException("pairing 2nd input mismatch");

        return pairingMap.pairing((Point) g2);
    }



    
    public PairingMap getPairingMap() {
        return pairingMap;
    }

    public void setPairingMap(PairingMap pairingMap) {
        this.pairingMap = pairingMap;
    }
}
