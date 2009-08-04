package it.unisa.dia.gas.plaf.jpbc.pairing.map;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Point;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public abstract class AbstractMillerPairingMap<E extends Element> extends AbstractPairingMap {


    protected final void lineStep(Point<E> f0,
                                  Element a, Element b, Element c,
                                  Element Vx, Element Vy,
                                  Element V1x, Element V1y,
                                  Element e0,
                                  E Qx, E Qy,
                                  Element f) {
        computeLine(a, b, c, Vx, Vy, V1x, V1y, e0);
        millerStep(f0, a, b, c, Qx, Qy);
        f.mul(f0);
    }

    protected final void tangentStep(Point<E> f0,
                                     Element a, Element b, Element c,
                                     Element Vx, Element Vy,
                                     Element curveA,
                                     Element e0,
                                     E Qx, E Qy,
                                     Element f) {
        computeTangent(a, b, c, Vx, Vy, curveA, e0);
        millerStep(f0, a, b, c, Qx, Qy);
        f.mul(f0);
    }

    protected final void tangentStepProjective(Point<E> f0,
                           Element a, Element b, Element c,
                           Element Vx, Element Vy, Element z,
                           Element z2, Element e0,
                           E Qx, E Qy,
                           Element f) {
        computeTangentProjective(a, b, c, Vx, Vy, z, z2, e0);
        millerStep(f0, a, b, c, Qx, Qy);
        f.mul(f0);
    }

    protected abstract void millerStep(Point<E> out,
                                       Element a, Element b, Element c,
                                       E Qx, E Qy);



    protected final void computeTangent(Element a, Element b, Element c,
                                        Element Vx, Element Vy,
                                        Element curveA,
                                        Element temp) {
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
        a.add(curveA);
        a.negate();

        b.set(Vy).twice();

        temp.set(b).mul(Vy);
        c.set(a).mul(Vx);
        c.add(temp).negate();
    }

    protected final void computeTangent(MillerPreProcessingInfo info,
                                        Element a, Element b, Element c,
                                        Element Vx, Element Vy,
                                        Element curveA,
                                        Element temp) {
        //a = -slope_tangent(Z.x, Z.y);
        //b = 1;
        //c = -(Z.y + a * Z.x);
        //but we multiply by 2*Z.y to avoid division

        //a = -Vx * (3 Vx + twicea_2) - a_4;
        //Common curves: a2 = 0 (and cc->a is a_4), so
        //a = -(3 Vx^2 + cc->a)
        //b = 2 * Vy
        //c = -(2 Vy^2 + a Vx);

        a.set(Vx).square();
//        a.add(a).add(a);
        a.mul(3);
        a.add(curveA);
        a.negate();

        b.set(Vy).twice();

        temp.set(b).mul(Vy);
        c.set(a).mul(Vx);
        c.add(temp).negate();

        info.addRow(a, b, c);
    }

    /**
     * Compute the tangent line T (aX + bY + c) at point V=(Vx, Vy, z)
     *
     * @param a  the coefficient of X of tangent line T.
     * @param b  the coefficient of Y of tangent line T.
     * @param c  the constant term f tangent line T.
     * @param Vx V's x.
     * @param Vy V's y.
     * @param z  V's z
     * @param z2 z square.
     * @param e0 temp element.
     */
    protected final void computeTangentProjective(Element a, Element b, Element c,
                                                  Element Vx, Element Vy, Element z,
                                                  Element z2, Element e0) {
        a.set(z2).square();
        b.set(Vx).square();
        a.add(b.add(e0.set(b).twice())).negate();

        // Now:
        // a = -(3x^2 + cca z^4)     with cca = 1

        e0.set(Vy).twice();
        b.set(e0).mul(z2).mul(z);

        // Now:
        // b = 2 y z^3

        c.set(Vx).mul(a);
        a.mul(z2);
        e0.mul(Vy);
        c.add(e0).negate();

        // Now:
        // a = -3x^2 z^2 - z^6
        // c = 3x^3 + z^4 x - 2x^2 y
    }


    /**
     * Compute the tangent line L (aX + bY + c) through the points V=(Vx, Vy) e V1=(V1x, V1y).
     *
     * @param a   the coefficient of X of tangent line T.
     * @param b   the coefficient of Y of tangent line T.
     * @param c   the constant term f tangent line T.
     * @param Vx  V's x.
     * @param Vy  V's y.
     * @param V1x V1's x.
     * @param V1y V1's y.
     * @param temp  temp element.
     */
    protected final void computeLine(Element a, Element b, Element c,
                                     Element Vx, Element Vy,
                                     Element V1x, Element V1y,
                                     Element temp) {

        // a = -(V1y - Vy) / (V1x - Vx);
        // b = 1;
        // c = -(Vy + a * Vx);
        //
        // but we will multiply by V1x - Vx to avoid division, so
        //
        // a = -(V1y - Vy)
        // b = V1x - Vx
        // c = -(Vy b + a Vx);

        a.set(Vy).sub(V1y);
        b.set(V1x).sub(Vx);
        c.set(Vx).mul(V1y).sub(temp.set(Vy).mul(V1x));
    }

    protected final void computeLine(MillerPreProcessingInfo info,
                                    Element a, Element b, Element c,
                                    Element Vx, Element Vy,
                                    Element V1x, Element V1y,
                                    Element temp) {
        // a = -(V1y - Vy) / (V1x - Vx);
        // b = 1;
        // c = -(Vy + a * Vx);
        //
        // but we will multiply by V1x - Vx to avoid division, so
        //
        // a = -(V1y - Vy)
        // b = V1x - Vx
        // c = -(Vy b + a Vx);

        a.set(Vy).sub(V1y);
        b.set(V1x).sub(Vx);
        c.set(Vx).mul(V1y).sub(temp.set(Vy).mul(V1x));

        info.addRow(a, b, c);
    }


    protected final Element lucasEven(Point in, BigInteger cofactor) {
        if (in.isOne()) {
            return in.duplicate();
        }

        Point out = (Point) in.getField().newElement();
        Point temp = (Point) in.getField().newElement();

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
        while (true) {
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

        v0.twice();
        in0.set(t1).mul(v1).sub(v0);

        t1.square().sub(t0).sub(t0);

        v0.set(v1).halve();
        v1.set(in0).div(t1);
        v1.mul(in1);

        return out;

        /*
        static void lucasEven(element_ptr out, element_ptr in, mpz_t cofactor)
//assumes cofactor is even
//mangles in
//in cannot be out
{
    if (element_is1(in)) {
      element_set(out, in);
      return;
    }
    element_t temp;
    element_init_same_as(temp, out);
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

    //assume cofactor = (q^2 - q + 1) / r is odd
    //thus v1 = V_k, v0 = V_{k-1}
    //     U = (P v1 - 2 v0) / (P^2 - 4)

    element_double(v0, v0);
    element_mul(in0, t1, v1);
    element_sub(in0, in0, v0);

    element_square(t1, t1);
    element_sub(t1, t1, t0);
    element_sub(t1, t1, t0);

    element_halve(v0, v1);
    element_div(v1, in0, t1);
    element_mul(v1, v1, in1);

    element_clear(temp);
    */
    }

    protected final void lucasOdd(Point out, Point in, Point temp, BigInteger cofactor) {
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


    public static class MillerPreProcessingInfo {
        public int numRow = 0;
        public Element[][] coeff;

        public void addRow(Element a, Element b, Element c) {
            coeff[numRow][0] = a.duplicate();
            coeff[numRow][1] = b.duplicate();
            coeff[numRow][2] = c.duplicate();
            numRow++;
        }
    }


}
