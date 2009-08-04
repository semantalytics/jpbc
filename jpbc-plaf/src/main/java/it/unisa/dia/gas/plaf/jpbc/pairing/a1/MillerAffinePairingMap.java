package it.unisa.dia.gas.plaf.jpbc.pairing.a1;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Point;
import it.unisa.dia.gas.plaf.jpbc.field.gt.GTFiniteElement;
import it.unisa.dia.gas.plaf.jpbc.field.gt.GTFiniteField;
import it.unisa.dia.gas.plaf.jpbc.pairing.map.AbstractMillerPairingMap;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class MillerAffinePairingMap extends AbstractMillerPairingMap<Element> {
    protected TypeA1Pairing pairing;


    public MillerAffinePairingMap(TypeA1Pairing pairing) {
        this.pairing = pairing;
    }


    public Element pairing(Point in1, Point in2) {
        Element Px = in1.getX();
        Element Py = in1.getY();

        Element Qx = in2.getX();
        Element Qy = in2.getY();

        Point V = (Point) in1.duplicate();
        Element Vx = V.getX();
        Element Vy = V.getY();

        Element a = pairing.Fp.newElement();
        Element b = pairing.Fp.newElement();
        Element c = pairing.Fp.newElement();
        Element curveA = pairing.Fp.newOneElement();
        Element e0 = pairing.Fp.newElement();

        Point<Element> f0 = pairing.Fq2.newElement();
        Point out = pairing.Fq2.newElement();
        Point f = pairing.Fq2.newOneElement();

        //TODO: sliding NAF
        for(int m = pairing.r.bitLength() - 2; m > 0; m--) {
            tangentStep(f0, a, b, c, Vx, Vy, curveA, e0, Qx, Qy, f);
            V.twice();

            if (pairing.r.testBit(m)) {
                lineStep(f0, a, b, c, Vx, Vy, Px, Py, e0, Qx, Qy, f);
                V.add(in1);
            }

            f.square();
        }
        tangentStep(f0, a, b, c, Vx, Vy, curveA, e0, Qx, Qy, f);

        // Tate exponentiation.
        // Simpler but slower:
        //   element_pow_mpz(out, f, p->tateexp);
        // Use this trick instead:
        f0.set(f).invert();
        f.getY().negate();
        f.mul(f0);
        out.set(f).pow(pairing.phikonr);

        /* We could use this instead but p->h is small so this does not help much
        a_tateexp(out, f, f0, p->h);
        */

        return new GTFiniteElement(this, (GTFiniteField) pairing.getGT(), out);
    }

    public void finalPow(Element element) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    @Override
    protected void millerStep(Point out, Element a, Element b, Element c, Element Qx, Element Qy) {
        // we will map Q via (x,y) --> (-x, iy)
        // hence:
        // Re(a Qx + b Qy + c) = -a Q'x + c and
        // Im(a Qx + b Qy + c) = b Q'y

        Element rePart = out.getX();
        Element imPart = out.getY();

        rePart.set(c).sub(imPart.set(a).mul(Qx));
        imPart.set(b).mul(Qy);
    }
}
