package it.unisa.dia.gas.plaf.jpbc.pairing.map;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingPreProcessing;
import it.unisa.dia.gas.jpbc.Point;
import it.unisa.dia.gas.plaf.jpbc.pairing.combiner.PairingAccumulator;
import it.unisa.dia.gas.plaf.jpbc.pairing.combiner.PairingAccumulatorFactory;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public abstract class AbstractPairingMap implements PairingMap {

    protected final Pairing pairing;

    protected AbstractPairingMap(Pairing pairing) {
        this.pairing = pairing;
    }


    public boolean isProductPairingSupported() {
        return false;
    }

    public Element pairing(Element[] in1, Element[] in2) {
        PairingAccumulator combiner = PairingAccumulatorFactory.getInstance().getPairingMultiplier(pairing);
        for(int i = 0; i < in1.length; i++)
            combiner.addPairing(in1[i], in2[i]);
        return combiner.doFinal();
    }

    public int getPairingPreProcessingLengthInBytes() {
        return pairing.getG1().getLengthInBytes();
    }

    public PairingPreProcessing pairing(final Point in1) {
        return new DummyPairingPreProcessing(in1);
    }

    public PairingPreProcessing pairing(byte[] source, int offset) {
        return new DummyPairingPreProcessing(source, offset);
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


    public class DummyPairingPreProcessing implements PairingPreProcessing {
        protected Point in1;

        public DummyPairingPreProcessing(Point in1) {
            this.in1 = in1;
        }

        public DummyPairingPreProcessing(byte[] source, int offset) {
            this.in1 = (Point) pairing.getG1().newElement();
            this.in1.setFromBytes(source, offset);
        }

        public Element pairing(Element in2) {
            return AbstractPairingMap.this.pairing(in1, (Point) in2);
        }

        public byte[] toBytes() {
            return in1.toBytes();
        }

    }

}
