package it.unisa.dia.gas.plaf.jpbc.pairing.map;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Point;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public abstract class DefaultPairingMap implements PairingMap {
    protected Point in1;


    public void initPairingPreProcessing(Point in1) {
        this.in1 = in1;
    }

    public Element pairing(Point in2) {
        return pairing(in1, in2);
    }


    protected final void lineStep(Point f0,
                                  Element a, Element b, Element c,
                                  Element Vx, Element Vy,
                                  Element V1x, Element V1y,
                                  Element e0,
                                  Element Qx, Element Qy,
                                  Element f) {
        computeLine(a, b, c, Vx, Vy, V1x, V1y, e0);
        a_miller_evalfn(f0, a, b, c, Qx, Qy);
        f.mul(f0);
    }

    protected final void tangentStep(Point f0,
                                     Element a, Element b, Element c,
                                     Element Vx, Element Vy,
                                     Element e0,
                                     Element Qx, Element Qy,
                                     Element f) {
        computeTangent(a, b, c, Vx, Vy, e0);
        a_miller_evalfn(f0, a, b, c, Qx, Qy);
        f.mul(f0);
    }


    protected final void a_miller_evalfn(Point out, Element a, Element b, Element c, Element Qx, Element Qy) {
        // we will map Q via (x,y) --> (-x, iy)
        // hence:
        // Re(a Qx + b Qy + c) = -a Q'x + c and
        // Im(a Qx + b Qy + c) = b Q'y

        Element rePart = out.getX();
        Element imPart = out.getY();

        rePart.set(c).sub(imPart.set(a).mul(Qx));
        imPart.set(b).mul(Qy);
    }

    protected final void pointToAffine(Element Vx, Element Vy, Element z, Element z2, Element e0) {
        // Vx = Vx * z^-2
        Vx.mul(e0.set(z.invert()).square());
        // Vy = Vy * z^-3
        Vy.mul(e0.mul(z));

        z.setToOne();
        z2.setToOne();
    }

    protected final void computeTangent(Element a, Element b, Element c, Element Vx, Element Vy, Element e0) {
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
     * @param e0  temp element.
     */
    protected final void computeLine(Element a, Element b, Element c,
                                     Element Vx, Element Vy,
                                     Element V1x, Element V1y,
                                     Element e0) {

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
        c.set(Vx).mul(V1y).sub(e0.set(Vy).mul(V1x));
    }


}
