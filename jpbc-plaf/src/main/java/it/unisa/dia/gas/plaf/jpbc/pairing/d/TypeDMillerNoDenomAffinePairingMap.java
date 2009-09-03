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
public class TypeDMillerNoDenomAffinePairingMap extends AbstractMillerPairingMap<Polynomial> {
    protected TypeDPairing pairing;
    protected MillerPreProcessingInfo processingInfo;


    public TypeDMillerNoDenomAffinePairingMap(TypeDPairing pairing) {
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

            return lucasEven(e0, pairing.phikonr);
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
        lucasEven(out, e0, pairing - > phikonr);

        element_clear(e0);
        element_clear(e2);
        element_clear(e3);
        }else{
            element_pow_mpz(out, in, p - > tateExp);
        }
        */
    }

    final void qPower(int sign, PolyModElement e2, Element e0re, Element e0im, Element e0re0, Element e0im0, List<Element> inre, List<Element> inim) {
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

    protected void millerStep(Point<Polynomial> out, Element a, Element b, Element c, Polynomial Qx, Polynomial Qy) {
        // a, b, c are in Fq
        // point Q is (Qx, Qy * sqrt(nqr)) where nqr is used to construct
        // the quadratic field extension Fqk of Fqd

        Polynomial rePart = out.getX();
        Polynomial imPart = out.getY();

        int i;
        //int d = rePart.getField().getN();
        int d = rePart.getDegree();
        for (i = 0; i < d; i++) {
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
                    TypeDMillerNoDenomAffinePairingMap.this,
                    (GTFiniteField) pairing.getGT(),
                    tatePow(out)
            );
        }
    }

}
