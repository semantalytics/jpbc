package it.unisa.dia.gas.plaf.jpbc.pairing.d;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.PairingPreProcessing;
import it.unisa.dia.gas.jpbc.Point;
import it.unisa.dia.gas.jpbc.Polynomial;
import it.unisa.dia.gas.plaf.jpbc.field.curve.CurveField;
import it.unisa.dia.gas.plaf.jpbc.field.gt.GTFiniteElement;
import it.unisa.dia.gas.plaf.jpbc.field.gt.GTFiniteField;
import it.unisa.dia.gas.plaf.jpbc.field.polymod.PolyModElement;
import it.unisa.dia.gas.plaf.jpbc.pairing.map.AbstractMillerPairingMap;

import java.util.List;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TypeDTateAffineNoDenomMillerPairingMap extends AbstractMillerPairingMap<Polynomial> {
    protected TypeDPairing pairing;


    public TypeDTateAffineNoDenomMillerPairingMap(TypeDPairing pairing) {
        this.pairing = pairing;
    }


    public Element pairing(Point in1, Point in2) {
        // map from twist: (x, y) --> (v^-1 x, v^-(3/2) y)
        // where v is the quadratic non-residue used to construct the twist
        Polynomial Qx = (Polynomial) in2.getX().duplicate().mul(pairing.nqrInverse);
        // v^-3/2 = v^-2 * v^1/2
        Polynomial Qy = (Polynomial) in2.getY().duplicate().mul(pairing.nqrInverseSquare);

        return new GTFiniteElement(this, (GTFiniteField) pairing.getGT(), tatePow(pairing(in1, Qx, Qy)));
    }

    public void finalPow(Element element) {
        element.set(tatePow(element));
    }

    public PairingPreProcessing pairingPreProcessing(Point in1) {
        return new TypeDMillerNoDenomAffinePairingPreProcessing(in1);
    }

    public boolean isAlmostCoddh(Element a, Element b, Element c, Element d) {
        // Twist: (x, y) --> (v^-1 x, v^-(3/2) y)
        // where v is the quadratic nonresidue used to construct the twist.

        Element cx = ((Point) c).getX().duplicate().mul(pairing.nqrInverse);
        Element dx = ((Point) d).getX().duplicate().mul(pairing.nqrInverse);

        // v^-3/2 = v^-2 * v^1/2
        Element cy = ((Point) c).getY().duplicate().mul(pairing.nqrInverseSquare);
        Element dy = ((Point) d).getY().duplicate().mul(pairing.nqrInverseSquare);

        Element t0 = pairing((Point) a, (Polynomial) dx, (Polynomial) dy);
        Element t1 = pairing((Point) b, (Polynomial) cx, (Polynomial) cy);
        t0 = tatePow(t0);
        t1 = tatePow(t1);
        Element t2 = t0.duplicate().mul(t1);

        if (t2.isOne())
            return true;    // We were given g, g^x, h, h^-x.
        else {
          // Cheaply check the other case.
            t2.set(t0).mul(t1.invert());
            if (t2.isOne())
                return true;    // We were given g, g^x, h, h^x.
        }

        return false;
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

            qPower(1, e2, e0re, e0im, e0re0, e0im0, inre, inmi);
            e3.set(e0);
            e0re.set(in.getX());
            e0im.set(in.getY()).negate();
            e3.mul(e0);
            qPower(-1, e2, e0re, e0im, e0re0, e0im0, inre, inmi);
            e0.mul(in);
            e0.invert();
            in.set(e3).mul(e0);
            e0.set(in);

            return lucasEven(e0, pairing.phikOnr);
        } else {
            return element.duplicate().pow(pairing.tateExp);
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
        qPower(1);
        element_set(e3, e0);
        element_set(e0re, fi_re(in));
        element_neg(e0im, fi_im(in));
        element_mul(e3, e3, e0);
        qPower(-1);
        element_mul(e0, e0, in);
        element_invert(e0, e0);
        element_mul(in, e3, e0);

        element_set(e0, in);
        lucasEven(out, e0, pairing - > phikOnr);

        element_clear(e0);
        element_clear(e2);
        element_clear(e3);
        }else{
            element_pow_mpz(out, in, p - > tateExp);
        }
        */
    }

    final void qPower(int sign, PolyModElement e2,
                      Element e0re, Element e0im, Element e0re0, Element e0im0,
                      List<Element> inre, List<Element> inim) {
        e2.set(pairing.xPowq).polymodConstMul(inre.get(1));
        e0re.set(e2);
        e2.set(pairing.xPowq2).polymodConstMul(inre.get(2));
        e0re.add(e2);
        e0re0.add(inre.get(0));

        if (sign > 0) {
            e2.set(pairing.xPowq).polymodConstMul(inim.get(1));
            e0im.set(e2);
            e2.set(pairing.xPowq2).polymodConstMul(inim.get(2));
            e0im.add(e2);
            e0im0.add(inim.get(0));
        } else {
            e2.set(pairing.xPowq).polymodConstMul(inim.get(1));
            e0im.set(e2).negate();
            e2.set(pairing.xPowq2).polymodConstMul(inim.get(2));
            e0im.sub(e2);
            e0im0.sub(inim.get(0));
        }
    }

    protected Element pairing(Point P, Polynomial Qx, Polynomial Qy) {
        Element Px = P.getX();
        Element Py = P.getY();

        Point Z = (Point) P.duplicate();
        Element Zx = Z.getX();
        Element Zy = Z.getY();

        Element a = Px.getField().newElement();
        Element b = a.duplicate();
        Element c = a.duplicate();
        Element cca = ((CurveField) P.getField()).getA();
        Element temp = a.duplicate();

        Point<Polynomial> f0 = pairing.Fqk.newElement();
        Element f = pairing.Fqk.newOneElement();

        for (int m = pairing.r.bitLength() - 2; m > 0; m--) {
            tangentStep(f0, a, b, c, Zx, Zy, cca, temp, Qx, Qy, f);
            Z.twice();

            if (pairing.r.testBit(m)) {
                lineStep(f0, a, b, c, Zx, Zy, Px, Py, temp, Qx, Qy, f);
                Z.add(P);
            }

            f.square();
        }
        tangentStep(f0, a, b, c, Zx, Zy, cca, temp, Qx, Qy, f);

        return f;
    }


    /* TODO
//do many millers at one time with affine coordinates.
static void cc_millers_no_denom_affine(element_t res, mpz_t q, element_t P[],
    element_t Qx[], element_t Qy[], int n_prod) {
  int m, i;
  element_t v;
  element_t a, b, c;
  element_t t0;
  element_t e0;
  const element_ptr cca = curve_a_coeff(P[0]);
  element_ptr Px, Py;
  element_t* Z=malloc(sizeof(element_t)*n_prod);
  element_ptr Zx, Zy;

 // TODO: when exactly is this not needed?
 // void do_vertical() {
  //  mapbase(e0, Z->x);
  //  element_sub(e0, Qx, e0);
    element_mul(v, v, e0);
  //}


  void do_tangents(void) {
    // a = -(3 Zx^2 + cc->a)
    // b = 2 * Zy
    // c = -(2 Zy^2 + a Zx);
    for(i=0; i<n_prod; i++){
      Px = curve_x_coord(P[i]);
      Py = curve_y_coord(P[i]);
      Zx = curve_x_coord(Z[i]);
      Zy = curve_y_coord(Z[i]);

      element_square(a, Zx);
      element_mul_si(a, a, 3);
      element_add(a, a, cca);
      element_neg(a, a);

      element_add(b, Zy, Zy);

      element_mul(t0, b, Zy);
      element_mul(c, a, Zx);
      element_add(c, c, t0);
      element_neg(c, c);

      d_miller_evalfn(e0, a, b, c, Qx[i], Qy[i]);
      element_mul(v, v, e0);
    }
  }

  void do_lines(void) {
    // a = -(B.y - A.y) / (B.x - A.x);
    // b = 1;
    // c = -(A.y + a * A.x);
    // but we multiply by B.x - A.x to avoid division.
    for(i=0; i<n_prod; i++){
      Px = curve_x_coord(P[i]);
      Py = curve_y_coord(P[i]);
      Zx = curve_x_coord(Z[i]);
      Zy = curve_y_coord(Z[i]);

      element_sub(b, Px, Zx);
      element_sub(a, Zy, Py);
      element_mul(t0, b, Zy);
      element_mul(c, a, Zx);
      element_add(c, c, t0);
      element_neg(c, c);

      d_miller_evalfn(e0, a, b, c, Qx[i], Qy[i]);
      element_mul(v, v, e0);
    }
  }

  Px= curve_x_coord(P[0]); //temporally used to initial a,b, c and etc.
  element_init(a, Px->field);
  element_init(b, a->field);
  element_init(c, a->field);
  element_init(t0, a->field);
  element_init(e0, res->field);

  element_init(v, res->field);
  for(i=0; i<n_prod; i++){
    element_init(Z[i], P[i]->field);
    element_set(Z[i], P[i]);
  }

  element_set1(v);
  m = mpz_sizeinbase(q, 2) - 2;

  for(;;) {
    do_tangents();

    if (!m) break;
    element_multi_double(Z, Z, n_prod); //Z_i=Z_i+Z_i for all i.

    if (mpz_tstbit(q, m)) {
      do_lines();
      element_multi_add(Z, Z, P, n_prod); //Z_i=Z_i+P_i for all i.
    }
    m--;
    element_square(v, v);
  }

  element_set(res, v);

  element_clear(v);
  for(i=0; i<n_prod; i++){
          element_clear(Z[i]);
  }
  free(Z);
  element_clear(a);
  element_clear(b);
  element_clear(c);
  element_clear(t0);
  element_clear(e0);
}


void cc_pairings_affine(element_ptr out, element_t in1[], element_t in2[],
        int n_prod, pairing_t pairing) {
  element_ptr Qbase;
  element_t* Qx=malloc(sizeof(element_t)*n_prod);
  element_t* Qy=malloc(sizeof(element_t)*n_prod);
  pptr p = pairing->data;
  int i;
  for(i=0; i<n_prod; i++){
          element_init(Qx[i], p->Fqd);
          element_init(Qy[i], p->Fqd);
        Qbase = in2[i];
          // Twist: (x, y) --> (v^-1 x, v^-(3/2) y)
          // where v is the quadratic nonresidue used to construct the twist.
          element_mul(Qx[i], curve_x_coord(Qbase), p->nqrinv);
          // v^-3/2 = v^-2 * v^1/2
          element_mul(Qy[i], curve_y_coord(Qbase), p->nqrinv2);
  }
  cc_millers_no_denom_affine(out, pairing->r, in1, Qx, Qy, n_prod);
  cc_tatepower(out, out, pairing);

  for(i=0; i<n_prod; i++){
          element_clear(Qx[i]);
                element_clear(Qy[i]);
  }
  free(Qx);
        free(Qy);

}

     */

    protected void millerStep(Point<Polynomial> out, Element a, Element b, Element c, Polynomial Qx, Polynomial Qy) {
        // a, b, c are in Fq
        // point Q is (Qx, Qy * sqrt(nqr)) where nqr is used to construct
        // the quadratic field extension Fqk of Fqd

        Polynomial rePart = out.getX();
        Polynomial imPart = out.getY();
        for (int i = 0, d = rePart.getDegree(); i < d; i++) {
            rePart.getCoefficient(i).set(Qx.getCoefficient(i)).mul(a);
            imPart.getCoefficient(i).set(Qy.getCoefficient(i)).mul(b);
        }
        rePart.getCoefficient(0).add(c);
    }


    public class TypeDMillerNoDenomAffinePairingPreProcessing implements PairingPreProcessing {
        protected Point in1;
        protected MillerPreProcessingInfo processingInfo;


        public TypeDMillerNoDenomAffinePairingPreProcessing(Point in1) {
            this.in1 = in1;

            Point P = in1;
            Element Px = P.getX();
            Element Py = P.getY();

            Point Z = (Point) P.duplicate();
            Element Zx = Z.getX();
            Element Zy = Z.getY();

            Element a = pairing.Fq.newElement();
            Element b = pairing.Fq.newElement();
            Element c = pairing.Fq.newElement();
            Element curveA = ((CurveField) P.getField()).getA();
            Element temp = pairing.Fq.newElement();

            int m = pairing.r.bitLength() - 2;
            processingInfo = new MillerPreProcessingInfo();
            processingInfo.coeff = new Element[2 * m][3];

            for (; m > 0; m--) {
                computeTangent(processingInfo, a, b, c, Zx, Zy, curveA, temp);
                Z.twice();

                if (pairing.r.testBit(m)) {
                    computeLine(processingInfo, a, b, c, Zx, Zy, Px, Py, temp);
                    Z.add(P);
                }
            }
            computeTangent(processingInfo, a, b, c, Zx, Zy, curveA, temp);
        }

        public Element pairing(Element in2) {
            Point pointIn2 = (Point) in2;

            // map from twist: (x, y) --> (v^-1 x, v^-(3/2) y)
            // where v is the quadratic non-residue used to construct the twist
            Polynomial Qx = (Polynomial) pointIn2.getX().duplicate().mul(pairing.nqrInverse);
            // v^-3/2 = v^-2 * v^1/2
            Polynomial Qy = (Polynomial) pointIn2.getY().duplicate().mul(pairing.nqrInverseSquare);

            Point<Polynomial> f0 = pairing.Fqk.newElement();
            Element out = pairing.Fqk.newOneElement();
            int row = 0;

            for (int m =  pairing.r.bitLength() - 2; m > 0; m--) {
                millerStep(f0, processingInfo.coeff[row][0], processingInfo.coeff[row][1], processingInfo.coeff[row][2], Qx, Qy);
                out.mul(f0);
                row++;

                if (pairing.r.testBit(m)) {
                    millerStep(f0, processingInfo.coeff[row][0], processingInfo.coeff[row][1], processingInfo.coeff[row][2], Qx, Qy);
                    out.mul(f0);
                    row++;
                }

                out.square();
            }
            millerStep(f0, processingInfo.coeff[row][0], processingInfo.coeff[row][1], processingInfo.coeff[row][2], Qx, Qy);
            out.mul(f0);

            return new GTFiniteElement(
                    TypeDTateAffineNoDenomMillerPairingMap.this,
                    (GTFiniteField) pairing.getGT(),
                    tatePow(out)
            );
        }
    }

}
