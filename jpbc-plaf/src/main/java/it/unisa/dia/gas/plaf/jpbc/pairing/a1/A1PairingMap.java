package it.unisa.dia.gas.plaf.jpbc.pairing.a1;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Point;
import it.unisa.dia.gas.plaf.jpbc.field.gt.GTFiniteElement;
import it.unisa.dia.gas.plaf.jpbc.field.gt.GTFiniteField;
import it.unisa.dia.gas.plaf.jpbc.pairing.map.DefaultPairingMap;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class A1PairingMap extends DefaultPairingMap {
    protected TypeA1Pairing pairing;


    public A1PairingMap(TypeA1Pairing pairing) {
        this.pairing = pairing;
    }


    public Element pairing(Point in1, Point in2) {
        Point V;
        int m;

        Element Px = in1.getX();
        Element Py = in1.getY();
        Element Qx = in2.getX();
        Element Qy = in2.getY();
        Element Vx;
        Element Vy;

        V = (Point) in1.duplicate();
        Vx = V.getX();
        Vy = V.getY();

        Element a = pairing.Fp.newElement();
        Element b = pairing.Fp.newElement();
        Element c = pairing.Fp.newElement();
        Element e0 = pairing.Fp.newElement();

        Point f0 = pairing.Fq2.newElement();
        Point out = pairing.Fq2.newElement();
        Point f = pairing.Fq2.newOneElement();

        m = pairing.r.bitLength() - 2;

        //TODO: sliding NAF
        for(;;) {
            do_tangent(f0, a, b, c, Vx, Vy, e0, Qx, Qy, f);
            if (m == 0)
                break;

            V.twice();
            if (pairing.r.testBit(m)) {
                do_line(f0, a, b, c, Vx, Vy, e0, Px, Py, Qx, Qy, f);
                V.add(in1);
            }

            m--;
            f.square();
        }

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




    final void do_tangent(Point f0, Element a, Element b, Element c, Element Vx, Element Vy, Element e0, Element Qx, Element Qy, Element f) {
        compute_abc_tangent(a, b, c, Vx, Vy, e0);
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

}
