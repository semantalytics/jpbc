package it.unisa.dia.gas.plaf.jpbc.pairing.map;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.PairingPreProcessing;
import it.unisa.dia.gas.jpbc.Point;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public abstract class AbstractPairingMap implements PairingMap {

    protected Point in1;


    public Element pairing(Element[] in1, Element[] in2) {
        Element out = pairing((Point) in1[0], (Point) in2[0]);

        for(int i = 1; i < in1.length; i++)
            out.mul(pairing((Point) in1[i], (Point) in2[i]));
        
        return out;
    }

    public PairingPreProcessing pairingPreProcessing(final Point in1) {
        return new PairingPreProcessing() {
            public Element pairing(Element in2) {
                return AbstractPairingMap.this.pairing(in1, (Point) in2);
            }
        };
    }

    public boolean isAlmostCoddh(Element a, Element b, Element c, Element d) {
        Element t0, t1;

        t0 = pairing((Point) a, (Point) d);
        t1 = pairing((Point) b, (Point) c);

        if (t0.isEqual(t1)) {
            return true;
        } else {
            t0.mul(t1);
            return t0.isOne();
        }
    }


    protected final void pointToAffine(Element Vx, Element Vy, Element z, Element z2, Element e0) {
        // Vx = Vx * z^-2
        Vx.mul(e0.set(z.invert()).square());
        // Vy = Vy * z^-3
        Vy.mul(e0.mul(z));

        z.setToOne();
        z2.setToOne();
    }

}
