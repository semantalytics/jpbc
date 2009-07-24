package it.unisa.dia.gas.plaf.jpbc.pairing.d;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Point;
import it.unisa.dia.gas.jpbc.Polynomial;
import it.unisa.dia.gas.plaf.jpbc.field.curve.CurveField;
import it.unisa.dia.gas.plaf.jpbc.field.gt.GTFiniteElement;
import it.unisa.dia.gas.plaf.jpbc.field.gt.GTFiniteField;
import it.unisa.dia.gas.plaf.jpbc.field.polymod.PolyModElement;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.PairingPreProcessingInfo;
import it.unisa.dia.gas.plaf.jpbc.pairing.map.PairingMap;

import java.math.BigInteger;
import java.util.List;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class CCPairingMap implements PairingMap {
    protected TypeDPairing pairing;
    protected PairingPreProcessingInfo processingInfo;


    public CCPairingMap(TypeDPairing pairing) {
        this.pairing = pairing;
    }


    public Element pairing(Point in1, Point in2) {
        // map from twist: (x, y) --> (v^-1 x, v^-(3/2) y)
        // where v is the quadratic non-residue used to construct the twist
        Polynomial Qx = (Polynomial) in2.getX().duplicate().mul(pairing.nqrInverse);

        // v^-3/2 = v^-2 * v^1/2
        Polynomial Qy = (Polynomial) in2.getY().duplicate().mul(pairing.nqrInverseSquare);

        return new GTFiniteElement(this, (GTFiniteField) pairing.getGT(), tatePow(cc_miller_no_denom_fn(pairing.r, in1, Qx, Qy)));
    }

    public void finalPow(Element element) {
        element.set(tatePow(element));
    }



    public void initPairingPreProcessing(Point in1) {
        Point P = in1;
        Element Px = P.getX();
        Element Py = P.getY();
        int m;

        BigInteger q = pairing.r;
        Element cca = ((CurveField) P.getField()).getA();

        Point Z = (Point) P.duplicate();
        Element Zx = Z.getX();
        Element Zy = Z.getY();

        Element a = pairing.Fq.newElement();
        Element b = pairing.Fq.newElement();
        Element c = pairing.Fq.newElement();
        Element t0 = pairing.Fq.newElement();

        m = q.bitLength() - 2;
        processingInfo = new PairingPreProcessingInfo();
        processingInfo.coeff = new Element[2 * m][3];

        for(;;) {
            pp_do_tangent(processingInfo, a, b,c, Zx, Zy, cca,t0);

            if (m == 0)
                break;

                Z.twice();
                if (q.testBit(m)) {
                    pp_do_line(processingInfo, a, b, c, Zx, Zy, Px, Py, t0);
                    Z.add(P);
                }
                m--;
        }
    }

    public Element pairing(Point in2) {
        BigInteger q = pairing.r;
        int m = q.bitLength() - 2;

        Point<Polynomial> e0 = pairing.Fqk.newElement();

        // map from twist: (x, y) --> (v^-1 x, v^-(3/2) y)
        // where v is the quadratic non-residue used to construct the twist
        Polynomial Qx = (Polynomial) in2.getX().duplicate().mul(pairing.nqrInverse);

        // v^-3/2 = v^-2 * v^1/2
        Polynomial Qy = (Polynomial) in2.getY().duplicate().mul(pairing.nqrInverseSquare);

        Element out = pairing.Fqk.newOneElement();
        int i = 0;
        for(;;) {
            d_miller_evalfn(e0, processingInfo.coeff[i][0], processingInfo.coeff[i][1], processingInfo.coeff[i][2], Qx, Qy);
            out.mul(e0);
            i++;

            if (m==0)
                break;

            if (q.testBit(m)) {
                d_miller_evalfn(e0, processingInfo.coeff[i][0], processingInfo.coeff[i][1], processingInfo.coeff[i][2], Qx, Qy);
                out.mul(e0);
                i++;
            }
            m--;
            out.square();
        }

        return new GTFiniteElement(this, (GTFiniteField) pairing.getGT(), tatePow(out));
    }


    void pp_do_tangent(PairingPreProcessingInfo info, Element a, Element b, Element c, Element Zx, Element Zy, Element cca, Element t0) {
    //a = -slope_tangent(Z.x, Z.y);
    //b = 1;
    //c = -(Z.y + a * Z.x);
    //but we multiply by 2*Z.y to avoid division

    //a = -Zx * (3 Zx + twicea_2) - a_4;
    //Common curves: a2 = 0 (and cc->a is a_4), so
    //a = -(3 Zx^2 + cc->a)
    //b = 2 * Zy
    //c = -(2 Zy^2 + a Zx);


        a.set(Zx).square();
        t0.set(a).twice();
        a.add(t0).add(cca).negate();

        b.set(Zy).add(Zy);

        t0.set(b).mul(Zy);
        c.set(a).mul(Zx);
        c.add(t0).negate();

        info.addRow(a, b, c);


    /*element_square(a, Zx);
    element_double(t0, a);
    element_add(a, a, t0);
    element_add(a, a, cca);
    element_neg(a, a);

    element_add(b, Zy, Zy);

    element_mul(t0, b, Zy);
    element_mul(c, a, Zx);
    element_add(c, c, t0);
    element_neg(c, c);

	store_abc();
    */
    }

    void pp_do_line(PairingPreProcessingInfo info, Element a, Element b, Element c, Element Zx, Element Zy, Element Px, Element Py, Element t0) {
	//a = -(B.y - A.y) / (B.x - A.x);
	//b = 1;
	//c = -(A.y + a * A.x);
	//but we'll multiply by B.x - A.x to avoid division

        b.set(Px).sub(Zx);
        a.set(Zy).sub(Py);
        t0.set(b).mul(Zy);
        c.set(a).mul(Zx);
        c.add(t0).negate();

        info.addRow(a,b,c);
	/*element_sub(b, Px, Zx);
	element_sub(a, Zy, Py);
	element_mul(t0, b, Zy);
	element_mul(c, a, Zx);
	element_add(c, c, t0);
	element_neg(c, c);

	store_abc();
	*/
    }

    public Element tatePow(Element element) {
        if (pairing.k == 6) {
            Point<Polynomial> e0, e3;
            PolyModElement e2;

            e0 = pairing.Fqk.newElement();
            e2 = pairing.Fqd.newElement();
            e3 = pairing.Fqk.newElement();

            Polynomial e0re = e0.getX();
            Polynomial e0im = e0.getY();

            Element e0re0 = e0re.getCoefficient(0);
            Element e0im0 = e0im.getCoefficient(0);

            Point<Polynomial> in = (Point<Polynomial>) element;
            List<Element> inre = in.getX().getCoefficients();
            List<Element> inmi = in.getY().getCoefficients();

            qpower(1, e2, e0re, e0im, e0re0, e0im0, inre, inmi);
            e3.set(e0);
            e0re.set(in.getX());
            e0im.set(in.getY()).negate();
            e3.mul(e0);
            qpower(-1, e2, e0re, e0im, e0re0, e0im0, inre, inmi);
            e0.mul(in);
            e0.invert();
            in.set(e3).mul(e0);
            e0.set(in);

            return lucas_even(e0, pairing.phikonr);
        } else {
            return element.duplicate().pow(pairing.tateexp);
        }

        /*
        static void cc_tatepower(element_ptr out, element_ptr in, pairing_t pairing)
        mnt_pairing_data_ptr p = pairing - > data;
        if (p - > k == 6) {
            element_t e0, e2, e3;
            element_init(e0, p - > Fqk);
            element_init(e2, p - > Fqd);
            element_init(e3, p - > Fqk);
            element_ptr e0re = fi_re(e0);
            element_ptr e0im = fi_im(e0);
            element_ptr e0re0 = ((element_t *) e0re - > data)[0];
            element_ptr e0im0 = ((element_t *) e0im - > data)[0];
            element_t * inre = fi_re(in) - > data;
            element_t * inim = fi_im(in) - > data;
        qpower(1);
        element_set(e3, e0);
        element_set(e0re, fi_re(in));
        element_neg(e0im, fi_im(in));
        element_mul(e3, e3, e0);
        qpower(-1);
        element_mul(e0, e0, in);
        element_invert(e0, e0);
        element_mul(in, e3, e0);

        element_set(e0, in);
        lucas_even(out, e0, pairing - > phikonr);

        element_clear(e0);
        element_clear(e2);
        element_clear(e3);
        }else{
            element_pow_mpz(out, in, p - > tateexp);
        }
        */
    }

















    final Element cc_miller_no_denom_fn(BigInteger q, Point P, Polynomial Qx, Polynomial Qy) {
        Element cca = ((CurveField) P.getField()).getA();

        Element Px = P.getX();
        Element Py = P.getY();

        Element a = Px.getField().newElement();
        Element b = a.duplicate();
        Element c = a.duplicate();
        Element t0 = a.duplicate();
        Point<Polynomial> e0 = pairing.Fqk.newElement();
        Element v = pairing.Fqk.newElement();

        Point Z = (Point) P.duplicate();
        Element Zx = Z.getX();
        Element Zy = Z.getY();

        v.setToOne();

        int m = q.bitLength() - 2;
        for (; ;) {
            do_tangent(a, b, c, Zx, Zy, cca, t0, e0, v, Qx, Qy);

            if (m == 0)
                break;

            Z.twice();
            if (q.testBit(m)) {
                do_line(a, b, c, Zx, Zy, cca, t0, e0, v, Qx, Qy, Px, Py);
                Z.add(P);
            }
            m--;
            v.square();
        }

        return v.duplicate();
    }

    final void do_tangent(Element a, Element b, Element c, Element Zx, Element Zy, Element cca, Element t0, Point<Polynomial> e0, Element v, Polynomial Qx, Polynomial Qy) {
        a.set(Zx).square().mul(3).add(cca).negate();
        b.set(Zy).add(Zy);
        t0.set(b).mul(Zy);
        c.set(a).mul(Zx).add(t0).negate();

        d_miller_evalfn(e0, a, b, c, Qx, Qy);

        v.mul(e0);
        /*
        //a = -(3 Zx^2 + cc->a)
        //b = 2 * Zy
        //c = -(2 Zy^2 + a Zx);

        element_square(a, Zx);
        element_mul_si(a, a, 3);
        element_add(a, a, cca);
        element_neg(a, a);

        element_add(b, Zy, Zy);

        element_mul(t0, b, Zy);
        element_mul(c, a, Zx);
        element_add(c, c, t0);
        element_neg(c, c);

        d_miller_evalfn(e0, a, b, c, Qx, Qy);
        element_mul(v, v, e0);
        */
    }

    final void do_line(Element a, Element b, Element c, Element Zx, Element Zy, Element cca, Element t0, Point<Polynomial> e0, Element v, Polynomial Qx, Polynomial Qy, Element Px, Element Py) {
        b.set(Px).sub(Zx);
        a.set(Zy).sub(Py);
        t0.set(b).mul(Zy);
        c.set(a).mul(Zx).add(t0).negate();

        d_miller_evalfn(e0, a, b, c, Qx, Qy);

        v.mul(e0);

        /*
        //a = -(B.y - A.y) / (B.x - A.x);
        //b = 1;
        //c = -(A.y + a * A.x);
        //but we'll multiply by B.x - A.x to avoid division

        element_sub(b, Px, Zx);
        element_sub(a, Zy, Py);
        element_mul(t0, b, Zy);
        element_mul(c, a, Zx);
        element_add(c, c, t0);
        element_neg(c, c);

        d_miller_evalfn(e0, a, b, c, Qx, Qy);
        element_mul(v, v, e0);
        */
    }

    final void d_miller_evalfn(Point<Polynomial> e0, Element a, Element b, Element c, Polynomial Qx, Polynomial Qy) {
        PolyModElement re_out = (PolyModElement) e0.getX();
        PolyModElement im_out = (PolyModElement) e0.getY();

        int i;
        int d = re_out.getField().getN();
        for (i = 0; i < d; i++) {
            re_out.getCoefficient(i).set(Qx.getCoefficient(i)).mul(a);
            im_out.getCoefficient(i).set(Qy.getCoefficient(i)).mul(b);
        }

        re_out.getCoefficient(0).add(c);

        /*
        //a, b, c are in Fq
        //point Q is (Qx, Qy * sqrt(nqr)) where nqr is used to construct
        //the quadratic field extension Fqk of Fqd
        element_ptr re_out = fi_re(e0);
        element_ptr im_out = fi_im(e0);

        int i;
        int d = polymod_field_degree(re_out->field);
        for (i=0; i<d; i++) {
            element_mul(polymod_coeff(re_out, i), polymod_coeff(Qx, i), a);
            element_mul(polymod_coeff(im_out, i), polymod_coeff(Qy, i), b);
        }
        element_add(polymod_coeff(re_out, 0), polymod_coeff(re_out, 0), c);
        */
    }

    //see thesis
    final void qpower(int sign, PolyModElement e2, Element e0re, Element e0im, Element e0re0, Element e0im0, List<Element> inre, List<Element> inim) {
        e2.set(pairing.xpowq).polymodConstMul(inre.get(1));
        e0re.set(e2);
        e2.set(pairing.xpowq2).polymodConstMul(inre.get(2));
        e0re.add(e2);
        e0re0.add(inre.get(0));

        if (sign > 0) {
            e2.set(pairing.xpowq).polymodConstMul(inim.get(1));
            e0im.set(e2);
            e2.set(pairing.xpowq2).polymodConstMul(inim.get(2));
            e0im.add(e2);
            e0im0.add(inim.get(0));
        } else {
            e2.set(pairing.xpowq).polymodConstMul(inim.get(1));
            e0im.set(e2).negate();
            e2.set(pairing.xpowq2).polymodConstMul(inim.get(2));
            e0im.sub(e2);
            e0im0.sub(inim.get(0));
        }

        /*
        polymod_const_mul(e2, inre[1], pairing.xpowq);
        element_set(e0re, e2);
        polymod_const_mul(e2, inre[2], pairing.xpowq2);
        element_add(e0re, e0re, e2);
        element_add(e0re0, e0re0, inre[0]);

        if (sign > 0) {
            polymod_const_mul(e2, inim[1], pairing.xpowq);
            element_set(e0im, e2);
            polymod_const_mul(e2, inim[2], pairing.xpowq2);
            element_add(e0im, e0im, e2);
            element_add(e0im0, e0im0, inim[0]);
        } else {
            polymod_const_mul(e2, inim[1], pairing.xpowq);
            element_neg(e0im, e2);
            polymod_const_mul(e2, inim[2], pairing.xpowq2);
            element_sub(e0im, e0im, e2);
            element_sub(e0im0, e0im0, inim[0]);
        }
        */
    }

    final Element lucas_even(Point in, BigInteger cofactor) {
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
        static void lucas_even(element_ptr out, element_ptr in, mpz_t cofactor)
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

}
