package it.unisa.dia.gas.plaf.jpbc.pairing.a;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Point;
import it.unisa.dia.gas.plaf.jpbc.field.gt.GTFiniteElement;
import it.unisa.dia.gas.plaf.jpbc.field.gt.GTFiniteField;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.DegreeTwoQuadraticElement;
import it.unisa.dia.gas.plaf.jpbc.pairing.map.AbstractMillerPairingMap;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TypeAMillerProjectivePairingMap extends AbstractMillerPairingMap {
    protected TypeAPairing pairing;
    protected MillerPreProcessingInfo processingInfo;


    public TypeAMillerProjectivePairingMap(TypeAPairing pairing) {
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
            tangentStepProjective(f0, a, b, c, Vx, Vy, z, z2, e0, Qx, Qy, f);
            twiceProjective(e0, a, b, c, Vx, Vy, z, z2);
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
            tangentStepProjective(f0, a, b, c, Vx, Vy, z, z2, e0, Qx, Qy, f);
            twiceProjective(e0, a, b, c, Vx, Vy, z, z2);
        }

        f.mul(f1);

        // Move to affine again
        pointToAffine(Vx, Vy, z, z2, e0);

        lineStep(f0, a, b, c, Vx, Vy, V1.getX(), V1.getY(), e0, Qx, Qy, f);

        // Do final pow
        Point out = pairing.Fq2.newElement();
        tatePow(out, f, f0, pairing.phikonr);

        return new GTFiniteElement(this, (GTFiniteField) pairing.getGT(), out);
    }

    public void finalPow(Element element) {
        Element t0, t1;
        t0 = element.getField().newElement();
        t1 = element.getField().newElement();

        tatePow((Point) t0, (Point) element, (Point) t1, pairing.phikonr);

        element.set(t0);
    }


    public void initPairingPreProcessing(Point in1) {
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

    public Element pairing(Point in2) {
        //TODO: use proj coords here too to shave off a little time
        Element Qx = in2.getX();
        Element Qy = in2.getY();
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

        tatePow(out, f, f0, pairing.phikonr);

        return new GTFiniteElement(this, (GTFiniteField) pairing.getGT(), out);
    }



    public Element tatePow(Element element) {
        Element t0, t1;
        t0 = element.getField().newElement();
        t1 = element.getField().newElement();

        tatePow((DegreeTwoQuadraticElement) t0, (DegreeTwoQuadraticElement) element, (DegreeTwoQuadraticElement) t1, pairing.phikonr);

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



    final void twiceProjective(Element e0, Element e1, Element e2, Element e3, Element Vx, Element Vy, Element z, Element z2) {
        e0.set(Vx).square().add(e1.set(e0).twice()).add(e1.set(z2).square());

        z.mul(Vy);
        z.twice();
        z2.set(z).square();

        e2.set(Vy).square();
        e1.set(Vx).mul(e2);
        e1.twice();
        e1.twice();

        e3.set(e1).twice();
        Vx.set(e0).square().sub(e3);

        e2.square().twice().twice().twice();

        e1.sub(Vx);
        e0.mul(e1);
        Vy.set(e0).sub(e2);

        /*
        //e0 = 3x^2 + (cc->a) z^4
        //for this case a = 1
        element_square(e0, Vx);
        ////element_mul_si(e0, e0, 3);
        element_double(e1, e0);
        element_add(e0, e1, e0);
        element_square(e1, z2);
        element_add(e0, e0, e1);

        //z_out = 2 y z
        element_mul(z, Vy, z);
        ////element_mul_si(z, z, 2);
        element_double(z, z);
        element_square(z2, z);

        //e1 = 4 x y^2
        element_square(e2, Vy);
        element_mul(e1, Vx, e2);
        //element_mul_si(e1, e1, 4);
        element_double(e1, e1);
        element_double(e1, e1);

        //x_out = e0^2 - 2 e1
        element_double(e3, e1);
        element_square(Vx, e0);
        element_sub(Vx, Vx, e3);

        //e2 = 8y^4
        element_square(e2, e2);
        //element_mul_si(e2, e2, 8);
        element_double(e2, e2);
        element_double(e2, e2);
        element_double(e2, e2);

        //y_out = e0(e1 - x_out) - e2
        element_sub(e1, e1, Vx);
        element_mul(e0, e0, e1);
        element_sub(Vy, e0, e2);
        */
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
