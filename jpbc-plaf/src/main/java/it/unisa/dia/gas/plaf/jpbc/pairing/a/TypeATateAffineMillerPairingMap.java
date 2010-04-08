package it.unisa.dia.gas.plaf.jpbc.pairing.a;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.PairingPreProcessing;
import it.unisa.dia.gas.jpbc.Point;
import it.unisa.dia.gas.plaf.jpbc.field.gt.GTFiniteElement;
import it.unisa.dia.gas.plaf.jpbc.field.gt.GTFiniteField;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.DegreeTwoQuadraticElement;
import it.unisa.dia.gas.plaf.jpbc.pairing.map.AbstractMillerPairingMap;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TypeATateAffineMillerPairingMap extends AbstractMillerPairingMap {
    protected TypeAPairing pairing;


    public TypeATateAffineMillerPairingMap(TypeAPairing pairing) {
        this.pairing = pairing;
    }


    /**
     * in1, in2 are from E(F_q), out from F_q^2
     */
    public Element pairing(Point in1, Point in2) {
        // could save a couple of inversions by avoiding
        // this function and rewriting lineStep() to handle projective coords
        // convert V from weighted projective (Jacobian) to affine
        // i.e. (X, Y, Z) --> (X/Z^2, Y/Z^3)
        // also sets z to 1

        Point V = (Point) in1.duplicate();
        Element Vx = V.getX();
        Element Vy = V.getY();
        Element z = pairing.Fq.newOneElement();
        Element z2 = pairing.Fq.newOneElement();

        Element Qx = in2.getX();
        Element Qy = in2.getY();

        // The coefficients of the line equation
        Element a = pairing.Fq.newElement();
        Element b = pairing.Fq.newElement();
        Element c = pairing.Fq.newElement();
        Element curveA = pairing.Fq.newOneElement();

        Point f0 = pairing.Fq2.newElement();
        Point f = pairing.Fq2.newOneElement();

        // Temp element
        Element e0 = pairing.Fq.newElement();

        // Remember that r = 2^exp2 + sign1 * 2^exp1 + sign0 * 1 (Solinas prime)

        int i = 0;
        int n = pairing.exp1;
        for (; i < n; i++) {
            // f = f^2 g_V,V(Q)
            // where g_V,V = tangent at V
            f.square();
            tangentStep(f0, a, b, c, Vx, Vy, curveA, e0, Qx, Qy, f);
            V.twice();
        }

        // Move to affine
        pointToAffine(Vx, Vy, z, z2, e0);

        Element f1;
        Point V1 = pairing.Eq.newElement();
        if (pairing.sign1 < 0) {
            V1.set(V).negate();
            f1 = f.duplicate().invert();
        } else {
            V1.set(V);
            f1 = f.duplicate();
        }

        n = pairing.exp2;
        for (; i < n; i++) {
            f.square();
            tangentStep(f0, a, b, c, Vx, Vy, curveA, e0, Qx, Qy, f);
            V.twice();
        }

        f.mul(f1);

        lineStep(f0, a, b, c, Vx, Vy, V1.getX(), V1.getY(), e0, Qx, Qy, f);

        // Do final pow
        Point out = pairing.Fq2.newElement();
        tatePow(out, f, f0, pairing.phikOnr);

        return new GTFiniteElement(this, (GTFiniteField) pairing.getGT(), out);
    }

    public void finalPow(Element element) {
        Element t0, t1;
        t0 = element.getField().newElement();
        t1 = element.getField().newElement();

        tatePow((Point) t0, (Point) element, (Point) t1, pairing.phikOnr);

        element.set(t0);
    }

    public PairingPreProcessing pairingPreProcessing(Point in1) {
        return new TypeAMillerAffinePairingPreProcessing(in1);
    }


    public Element tatePow(Element element) {
        Element t0, t1;
        t0 = element.getField().newElement();
        t1 = element.getField().newElement();

        tatePow((DegreeTwoQuadraticElement) t0, (DegreeTwoQuadraticElement) element, (DegreeTwoQuadraticElement) t1, pairing.phikOnr);

        element.set(t0);

        return element;
    }

    final void tatePow(Point out, Point in, Point temp, BigInteger cofactor) {
        Element in1 = in.getY();
        //simpler but slower:
        //element_pow_mpz(out, f, tateExp);

        //1. Exponentiate by q-1
        //which is equivalent to the following

        temp.set(in).invert();
        in1.negate();
        in.mul(temp);

/*        element_invert(temp, in);
        element_neg(in1, in1);
        element_mul(in, in, temp);
  */
        //2. Exponentiate by (q+1)/r

        //Instead of:
        //	element_pow_mpz(out, in, cofactor);
        //we use Lucas sequences (see "Compressed Pairings", Scott and Barreto)
        lucasOdd(out, in, temp, cofactor);
    }
    
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


    public class TypeAMillerAffinePairingPreProcessing implements PairingPreProcessing {
        protected Point in1;
        protected MillerPreProcessingInfo processingInfo;


        public TypeAMillerAffinePairingPreProcessing(Point in1) {
            this.in1 = in1;

            int i, n;

            processingInfo = new MillerPreProcessingInfo();
            processingInfo.coeff = new Element[pairing.exp2 + 1][3];

            Point V = (Point) in1.duplicate();
            Point V1 = pairing.Eq.newElement();

            Element Vx = V.getX();
            Element Vy = V.getY();

            Element V1x = V1.getX();
            Element V1y = V1.getY();

            Element a = pairing.Fq.newElement();
            Element b = pairing.Fq.newElement();
            Element c = pairing.Fq.newElement();
            Element curveA = pairing.Fq.newOneElement();
            Element temp = pairing.Fq.newElement();

            n = pairing.exp1;
            for (i = 0; i < n; i++) {
                computeTangent(processingInfo, a, b, c, Vx, Vy, curveA, temp);
                V.twice();
            }

            if (pairing.sign1 < 0) {
                V1.set(V).negate();
            } else {
                V1.set(V);
            }

            n = pairing.exp2;
            for (; i < n; i++) {
                computeTangent(processingInfo, a, b, c, Vx, Vy, curveA, temp);
                V.twice();
            }

            computeLine(processingInfo, a, b, c, Vx, Vy, V1x, V1y, temp);
        }

        public Element pairing(Element in2) {
            //TODO: use proj coords here too to shave off a little time
            Point pointIn2 = (Point) in2;

            Element Qx = pointIn2.getX();
            Element Qy = pointIn2.getY();
            int i, n;
            Point f0 = pairing.Fq2.newElement();
            Point f = pairing.Fq2.newOneElement();
            Point out = pairing.Fq2.newElement();

            n = pairing.exp1;
            for (i = 0; i < n; i++) {
                f.square();
                millerStep(f0, processingInfo.coeff[i][0], processingInfo.coeff[i][1], processingInfo.coeff[i][2], Qx, Qy);
                f.mul(f0);
            }
            if (pairing.sign1 < 0) {
                out.set(f).invert();
            } else {
                out.set(f);
            }
            n = pairing.exp2;
            for (; i < n; i++) {
                f.square();

                millerStep(f0, processingInfo.coeff[i][0], processingInfo.coeff[i][1], processingInfo.coeff[i][2], Qx, Qy);

                f.mul(f0);
            }

            f.mul(out);
            millerStep(f0, processingInfo.coeff[i][0], processingInfo.coeff[i][1], processingInfo.coeff[i][2], Qx, Qy);
            f.mul(f0);

            tatePow(out, f, f0, pairing.phikOnr);

            return new GTFiniteElement(
                    TypeATateAffineMillerPairingMap.this,
                    (GTFiniteField) pairing.getGT(),
                    out
            );
        }
    }
}