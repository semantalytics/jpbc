package it.unisa.dia.gas.plaf.jpbc.pairing.a;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Point;
import it.unisa.dia.gas.plaf.jpbc.field.gt.GTFiniteElement;
import it.unisa.dia.gas.plaf.jpbc.field.gt.GTFiniteField;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticTwoElement;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingMap;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class ProjectionPairingMap implements PairingMap {
    protected TypeAPairing pairing;
    protected PairingPreProcessingInfo processingInfo;


    public ProjectionPairingMap(TypeAPairing pairing) {
        this.pairing = pairing;
    }

    /**
     * in1, in2 are from E(F_q), out from F_q^2
     */
    public Element pairing(Point in1, Point in2) {
        //could save a couple of inversions by avoiding
        //this function and rewriting do_line() to handle projective coords
        //convert V from weighted projective (Jacobian) to affine
        //i.e. (X, Y, Z) --> (X/Z^2, Y/Z^3)
        //also sets z to 1

        Point V = (Point) in1.duplicate();
        Element Vx = V.getX();
        Element Vy = V.getY();

        Point V1 = pairing.Eq.newElement();
        Element V1x;
        Element V1y;

        Element Qx = in2.getX();
        Element Qy = in2.getY();

        Element a = pairing.Fq.newElement();
        Element b = pairing.Fq.newElement();
        Element c = pairing.Fq.newElement();
        Element e0 = pairing.Fq.newElement();

        Point f0 = pairing.Fq2.newElement();
        Point out = pairing.Fq2.newElement();
        Point f = pairing.Fq2.newOneElement();

        Element z = pairing.Fq.newElement().setToOne();
        Element z2 = pairing.Fq.newElement().setToOne();

        int i = 0;
        int n = pairing.exp1;
        for (; i < n; i++) {
            // f = f^2 g_V,V(Q)
            // where g_V,V = tangent at V
            f.square();

            do_tangent(f0, a, b, c, Vx, Vy, z, z2, e0, Qx, Qy, f);
            proj_double(e0, a, b, c, Vx, Vy, z, z2);
        }

        point_to_affine(z, e0, Vx, Vy, z2);

        Element f1;
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

            do_tangent(f0, a, b, c, Vx, Vy, z, z2, e0, Qx, Qy, f);
            proj_double(e0, a, b, c, Vx, Vy, z, z2);
        }

        f.mul(f1);

        point_to_affine(z, e0, Vx, Vy, z2);

        V1x = V1.getX();
        V1y = V1.getY();

        do_line(f0, a, b, c, Vx, Vy, e0, V1x, V1y, Qx, Qy, f);

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


    public void initPairingPreProcessing(Element in1) {
        int i, n;

        processingInfo = new PairingPreProcessingInfo();
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
        Element e0 = pairing.Fq.newElement();

        n = pairing.exp1;
        for (i=0; i<n; i++) {
            initPPPDoTangent(processingInfo, a, b, c, Vx, Vy, e0);
            V.twice();
        }

        if (pairing.sign1 < 0) {
            V1.set(V).negate();
        } else {
            V1.set(V);
        }

        n = pairing.exp2;
        for (; i<n; i++) {
            initPPPDoTangent(processingInfo, a, b, c, Vx, Vy, e0);
            V.twice();
        }

        initPPPDoLine(processingInfo, a, b, c, Vx, Vy, V1x, V1y, e0);
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
        for (i=0; i<n; i++) {
            f.square();
            a_miller_evalfn(f0, processingInfo.coeff[i][0], processingInfo.coeff[i][1], processingInfo.coeff[i][2], Qx, Qy);
            f.mul(f0);
        }
        if (pairing.sign1 < 0) {
            out.set(f).invert();
        } else {
            out.set(f);
        }
        n = pairing.exp2;
        for (; i<n; i++) {
            f.square();

            a_miller_evalfn(f0, processingInfo.coeff[i][0], processingInfo.coeff[i][1], processingInfo.coeff[i][2], Qx, Qy);

            f.mul(f0);
        }

        f.mul(out);
        {
            a_miller_evalfn(f0, processingInfo.coeff[i][0], processingInfo.coeff[i][1], processingInfo.coeff[i][2], Qx, Qy);
            f.mul(f0);
        }

        tatePow(out, f, f0, pairing.phikonr);

        return new GTFiniteElement(this, (GTFiniteField) pairing.getGT(), out);
    }


    

    final void initPPPDoLine(PairingPreProcessingInfo info, Element a, Element b, Element c, Element Vx, Element Vy, Element V1x, Element V1y, Element e0) {
        compute_abc_line(a, b, c, Vx, Vy, V1x, V1y, e0);
        info.addRow(a,b,c);
    }

    void initPPPDoTangent(PairingPreProcessingInfo info, Element a, Element b, Element c, Element Vx, Element Vy, Element e0) {
	    compute_abc_tangent(a, b, c, Vx, Vy, e0);
        info.addRow(a,b,c);
    }

    void compute_abc_tangent(Element a, Element b, Element c, Element Vx, Element Vy, Element e0) {
        //a = -slope_tangent(V.x, V.y);
        //b = 1;
        //c = -(V.y + aV.x);
        //but we multiply by -2*V.y to avoid division so:
        //a = -(3 Vx^2 + cc->a)
        //b = 2 * Vy
        //c = -(2 Vy^2 + a Vx);

        a.set(Vx).square();
//        a.add(a).add(a);
        a.mul(3);
        a.add(b.setToOne());
        a.negate();

        b.set(Vy).twice();

        e0.set(b).mul(Vy);
        c.set(a).mul(Vx);
        c.add(e0).negate();

        /*
        element_square(a, Vx);
        //element_mul_si(a, a, 3);
        element_add(e0, a, a);
        element_add(a, e0, a);
        element_set1(b);
        element_add(a, a, b);
        element_neg(a, a);

        element_double(b, Vy);

        element_mul(e0, b, Vy);
        element_mul(c, a, Vx);
        element_add(c, c, e0);
        element_neg(c, c);
        */
    }









    public Element tatePow(Element element) {
        Element t0, t1;
        t0 = element.getField().newElement();
        t1 = element.getField().newElement();

        tatePow((QuadraticTwoElement) t0, (QuadraticTwoElement) element, (QuadraticTwoElement) t1, pairing.phikonr);

        element.set(t0);

        return element;
    }

    final void tatePow(Point out, Point in, Point temp, BigInteger cofactor) {
        Element in1 = in.getY();
        //simpler but slower:
        //element_pow_mpz(out, f, tateexp);

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
        lucas_odd(out, in, temp, cofactor);
    }



    final void point_to_affine(Element z, Element e0, Element Vx, Element Vy, Element z2) {
        z.invert();
        e0.set(z).square();
        Vx.mul(e0);
        e0.mul(z);
        Vy.mul(e0);
        z.setToOne();
        z2.setToOne();

        /*
        element_invert(z, z);
        element_square(e0, z);
        element_mul(Vx, Vx, e0);
        element_mul(e0, e0, z);
        element_mul(Vy, Vy, e0);
        element_set1(z);
        element_set1(z2);
        */
    }

    final void proj_double(Element e0, Element e1, Element e2, Element e3, Element Vx, Element Vy, Element z, Element z2) {
        e0.set(Vx).square();
        e1.set(e0).twice();
        e0.add(e1);
        e1.set(z2).square();
        e0.add(e1);

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

    final void do_tangent(Point f0, Element a, Element b, Element c, Element Vx, Element Vy, Element z, Element z2, Element e0,
                          Element Qx, Element Qy,
                          Element f) {
        compute_abc_tangent_proj(a, b, c, Vx, Vy, z, z2, e0);
        a_miller_evalfn(f0, a, b, c, Qx, Qy);
        f.mul(f0);

        /*
        compute_abc_tangent_proj(a, b, c, Vx, Vy, z, z2, e0);
        a_miller_evalfn(f0, a, b, c, Qx, Qy);
        element_mul(f, f, f0);
        */
    }

    final void do_line(Point f0,
                       Element a, Element b, Element c, Element Vx, Element Vy, Element e0,
                       Element V1x, Element V1y,
                       Element Qx, Element Qy,
                       Element f) {
        compute_abc_line(a, b, c, Vx, Vy, V1x, V1y, e0);
        a_miller_evalfn(f0, a, b, c, Qx, Qy);
        f.mul(f0);

        /*
        compute_abc_line(a, b, c, Vx, Vy, V1x, V1y, e0);
	    a_miller_evalfn(f0, a, b, c, Qx, Qy);
	    element_mul(f, f, f0);
	    */
    }

    final void compute_abc_tangent_proj(Element a, Element b, Element c,
                                        Element Vx, Element Vy,
                                        Element z, Element z2, Element e0) {
        a.set(z2).square();
        b.set(Vx).square();

        a.add(b.add(e0.set(b).twice())).negate();

        e0.set(Vy).twice();
        b.set(e0).mul(z2).mul(z);

        c.set(Vx).mul(a);
        a.mul(z2);
        e0.mul(Vy);
        c.add(e0).negate();

        /*
        //a = -(3x^2 + cca z^4)
        //for this case cca = 1
        //b = 2 y z^3
        //c = -(2 y^2 + x a)
        //a = z^2 a
        element_square(a, z2);
        element_square(b, Vx);
        ////element_mul_si(b, b, 3);
        element_double(e0, b);
        element_add(b, e0, b);
        element_add(a, a, b);
        element_neg(a, a);

        ////element_mul_si(e0, Vy, 2);
        element_double(e0, Vy);
        element_mul(b, e0, z2);
        element_mul(b, b, z);

        element_mul(c, Vx, a);
        element_mul(a, a, z2);
        element_mul(e0, e0, Vy);
        element_add(c, c, e0);
        element_neg(c, c);
        */

    }

    final void compute_abc_line(Element a, Element b, Element c,
                                Element Vx, Element Vy,
                                Element V1x, Element V1y,
                                Element e0) {

        a.set(Vy).sub(V1y);
        b.set(V1x).sub(Vx);
        c.set(Vx).mul(V1y).sub(e0.set(Vy).mul(V1x));

        /*
        //a = -(B.y - A.y) / (B.x - A.x);
        //b = 1;
        //c = -(A.y + a * A.x);
        //but we'll multiply by B.x - A.x to avoid division, so
        //a = -(By - Ay)
        //b = Bx - Ax
        //c = -(Ay b + a Ax);
        element_sub(a, Vy, V1y);
        element_sub(b, V1x, Vx);
        element_mul(c, Vx, V1y);
        element_mul(e0, Vy, V1x);
        element_sub(c, c, e0);
        */
    }

    final void a_miller_evalfn(Point out, Element a, Element b, Element c, Element Qx, Element Qy) {
        //we'll map Q via (x,y) --> (-x, iy)
        //hence Re(a Qx + b Qy + c) = -a Q'x + c and
        //Im(a Qx + b Qy + c) = b Q'y
        Element x = out.getX();
        Element y = out.getY();

        x.set(c).sub(y.set(a).mul(Qx));
        y.set(b).mul(Qy);

/*        element_mul(fi_im(out), a, Qx);
        element_sub(fi_re(out), c, fi_im(out));
        element_mul(fi_im(out), b, Qy);*/
    }

    final void lucas_odd(Point out, Point in, Point temp, BigInteger cofactor) {
        //assumes cofactor is odd
        //overwrites in and temp, out must not be in
        //luckily this touchy routine is only used internally
        //TODO: rewrite to allow (out == in)? would simplify a_finalpow()

        Element in0 = in.getX();
        Element in1 = in.getY();

        Element v0 = out.getX();
        Element v1 = out.getY();

        Element t0 = temp.getX();
        Element t1 = temp.getY();

        t0.set(2);
        t1.set(in0).twice();

        v0.set(t0);
        v1.set(t1);

        int j = cofactor.bitLength() - 1;
        for (; ;) {
            if (j == 0) {
                v1.mul(v0).sub(t1);
                v0.square().sub(t0);

                break;
            }

            if (cofactor.testBit(j)) {
                v0.mul(v1).sub(t1);
                v1.square().sub(t0);

            } else {
                v1.mul(v0).sub(t1);
                v0.square().sub(t0);
            }
            j--;
        }

        v1.twice().sub(in0.set(v0).mul(t1));

        t1.square().sub(t0).sub(t0);
        v1.div(t1);

        v0.halve();
        v1.mul(in1);

        /*
        element_ptr in0 = fi_re(in);
        element_ptr in1 = fi_im(in);
        element_ptr v0 = fi_re(out);
        element_ptr v1 = fi_im(out);
        element_ptr t0 = fi_re(temp);
        element_ptr t1 = fi_im(temp);
        int j;

        element_set_si(t0, 2);
        element_double(t1, in0);

        element_set(v0, t0);
        element_set(v1, t1);

        j = mpz_sizeinbase(cofactor, 2) - 1;

        printf("J = %d",j);

        for (;;) {
        if (!j) {
            element_mul(v1, v0, v1);
            element_sub(v1, v1, t1);
            element_square(v0, v0);
            element_sub(v0, v0, t0);
            break;
        }
        if (mpz_tstbit(cofactor, j)) {
            element_mul(v0, v0, v1);
            element_sub(v0, v0, t1);
            element_square(v1, v1);
            element_sub(v1, v1, t0);
        } else {
            element_mul(v1, v0, v1);
            element_sub(v1, v1, t1);
            element_square(v0, v0);
            element_sub(v0, v0, t0);
        }
        j--;
        }

        //assume cofactor = (q + 1) / r is even
        //(r should be odd and q + 1 is always even)
        //thus v0 = V_k, v1 = V_{k+1}
        //and V_{k-1} = P v0 - v1

        //so U_k = (P V_k - 2 V_{k-1}) / (P^2 - 4)
        //       = (2 v1 - P v0) / (P^2 - 4)

        element_mul(in0, v0, t1);
        element_double(v1, v1);
        element_sub(v1, v1, in0);

        element_square(t1, t1);
        element_sub(t1, t1, t0);
        element_sub(t1, t1, t0);
        element_div(v1, v1, t1);

        element_halve(v0, v0);
        element_mul(v1, v1, in1);
        */
    }


}
