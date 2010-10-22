package it.unisa.dia.gas.plaf.jpbc.pairing.a1;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Point;
import it.unisa.dia.gas.plaf.jpbc.field.curve.CurveElement;
import it.unisa.dia.gas.plaf.jpbc.field.gt.GTFiniteElement;
import it.unisa.dia.gas.plaf.jpbc.field.gt.GTFiniteField;
import it.unisa.dia.gas.plaf.jpbc.pairing.map.AbstractMillerPairingMap;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TypeA1TateAffineMillerPairingMap extends AbstractMillerPairingMap<Element> {
    protected TypeA1Pairing pairing;


    public TypeA1TateAffineMillerPairingMap(TypeA1Pairing pairing) {
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
        //   element_pow_mpz(out, f, p->tateExp);
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
        Element t0, t1;
        t0 = element.getField().newElement();
        t1 = element.getField().newElement();

        tatePow((Point) t0, (Point) element, (Point) t1, pairing.phikonr);

        element.set(t0);
    }

    public Element pairing(Element[] in1, Element[] in2) {
        Field refField = in1[0].getField();

        CurveElement[] Vs = new CurveElement[in1.length];

        for(int i=0; i< in1.length; i++){
            Vs[i] = (CurveElement) in1[i].duplicate();
        }

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
            tangentStep(f0, a, b, c, Vs, curveA, e0, in2, f);

            refField.twice(Vs);

            if (pairing.r.testBit(m)) {
                lineStep(f0, a, b, c, Vs, in1, e0, in2, f);
                refField.add(Vs, in1);
            }

            f.square();
        }
        tangentStep(f0, a, b, c, Vs, curveA, e0, in2, f);

        // Tate exponentiation.
        // Simpler but slower:
        //   element_pow_mpz(out, f, p->tateExp);
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

    public Element tatePow(Element element) {
        Element t0, t1;
        t0 = element.getField().newElement();
        t1 = element.getField().newElement();

        tatePow((Point) t0, (Point) element, (Point) t1, pairing.phikonr);

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


    /*              TODO
//in1, in2 are from E(F_q), out from F_q^2
void a1_pairings_affine(element_ptr out, element_t in1[], element_t in2[],
    int n_prod, pairing_t pairing) {
  a1_pairing_data_ptr p = pairing->data;
  element_t* V = malloc(sizeof(element_t)*n_prod);
  element_t f, f0;
  element_t a, b, c;
  element_t e0;
  int m, i;
  element_ptr Px, Py;
  element_ptr Qx, Qy;
  element_ptr Vx, Vy;

  void do_tangents(void) {
    for(i=0; i<n_prod; i++){
      Vx = curve_x_coord(V[i]);
      Vy = curve_y_coord(V[i]);
      Qx = curve_x_coord(in2[i]);
      Qy = curve_y_coord(in2[i]);
      compute_abc_tangent(a, b, c, Vx, Vy, e0);
      a_miller_evalfn(f0, a, b, c, Qx, Qy);
      element_mul(f, f, f0);
    }
  }

  void do_lines(void) {
    for(i=0; i<n_prod; i++){
      Vx = curve_x_coord(V[i]);
      Vy = curve_y_coord(V[i]);
      Px = curve_x_coord(in1[i]);
      Py = curve_y_coord(in1[i]);
      Qx = curve_x_coord(in2[i]);
      Qy = curve_y_coord(in2[i]);
      compute_abc_line(a, b, c, Vx, Vy, Px, Py, e0);
      a_miller_evalfn(f0, a, b, c, Qx, Qy);
      element_mul(f, f, f0);
    }
  }

  for(i=0; i<n_prod; i++){
    element_init(V[i], p->Ep);
    element_set(V[i], in1[i]);
  }
  element_init(f, p->Fp2);
  element_init(f0, p->Fp2);
  element_set1(f);
  element_init(a, p->Fp);
  element_init(b, p->Fp);
  element_init(c, p->Fp);
  element_init(e0, p->Fp);

  m = mpz_sizeinbase(pairing->r, 2) - 2;

  //TODO: sliding NAF
  for(;;) {
    do_tangents();
    if (!m) break;
    element_multi_double(V, V, n_prod);
    if (mpz_tstbit(pairing->r, m)) {
      do_lines();
      element_multi_add(V, V, in1, n_prod);
    }

    m--;
    element_square(f, f);
  }

  // Tate exponentiation.
  // Simpler but slower:
  //   element_pow_mpz(out, f, p->tateexp);
  // Use this trick instead:
  element_invert(f0, f);
  element_neg(element_y(f), element_y(f));
  element_mul(f, f, f0);
  element_pow_mpz(out, f, pairing->phikonr);

  // We could use this instead but p->h is small so this does not help much
  // a_tateexp(out, f, f0, p->h);

  element_clear(f);
  element_clear(f0);
  for(i=0; i<n_prod; i++){
    element_clear(V[i]);
  }
  free(V);
  element_clear(a);
  element_clear(b);
  element_clear(c);
  element_clear(e0);
}
*/


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
