package it.unisa.dia.gas.plaf.jpbc.pairing.d;

import it.unisa.dia.gas.jpbc.*;
import it.unisa.dia.gas.plaf.jpbc.field.curve.CurveElement;
import it.unisa.dia.gas.plaf.jpbc.field.curve.CurveField;
import it.unisa.dia.gas.plaf.jpbc.field.gt.GTFiniteField;
import it.unisa.dia.gas.plaf.jpbc.field.naive.NaiveField;
import it.unisa.dia.gas.plaf.jpbc.field.poly.PolyElement;
import it.unisa.dia.gas.plaf.jpbc.field.poly.PolyField;
import it.unisa.dia.gas.plaf.jpbc.field.polymod.PolyModElement;
import it.unisa.dia.gas.plaf.jpbc.field.polymod.PolyModField;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticEvenField;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingMap;

import java.math.BigInteger;
import java.util.List;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TypeDPairing implements Pairing {
    protected CurveParams curveParams;

    protected int k;

    protected BigInteger r;
    protected BigInteger q;
    protected BigInteger h;
    protected BigInteger a;
    protected BigInteger b;

    protected PolyModElement xpowq, xpowq2;
    protected Element nqrInverse, nqrInverseSquare;
    protected BigInteger tateexp;

    protected BigInteger phikonr;

    protected Field Fq, Fqx;
    protected Field<? extends Point<Polynomial>> Fqk;
    protected PolyModField Fqd;
    protected CurveField Eq, Etwist;

    protected Field G1, G2, GT, Zr;

    protected PairingMap pairingMap;

    public TypeDPairing(CurveParams curveParams) {
        this.curveParams = curveParams;

        initParams();
        initMap();
        initFields();
    }


    public boolean isSymmetric() {
        return false;
    }

    public Field<? extends Point> getG1() {
        return G1;
    }

    public Field<? extends Point> getG2() {
        return G2;
    }

    public Field getZr() {
        return Zr;
    }

    public Field getGT() {
        return GT;
    }

    public Element pairing(Element g1, Element g2) {
        if (!G1.equals(g1.getField()))
            throw new IllegalArgumentException("pairing 1st input mismatch");
        if (!G2.equals(g2.getField()))
            throw new IllegalArgumentException("pairing 2nd input mismatch");

        if (g1.isZero() || g2.isZero())
            return GT.newElement().setToZero();

        return pairingMap.pairing((CurveElement) g1, (CurveElement) g2);
    }


    protected void initParams() {
        // validate the type
        String type = curveParams.getType();
        if (type == null || !"d".equalsIgnoreCase(type))
            throw new IllegalArgumentException("Type not valid. Found '" + type + "'. Expected 'd'.");

        // load params
        k = curveParams.getInt("k");
        if (k % 2 != 0)
            throw new IllegalArgumentException("odd k not implemented anymore");


        r = curveParams.getBigInteger("r");
        q = curveParams.getBigInteger("q");
        h = curveParams.getBigInteger("h");

        a = curveParams.getBigInteger("a");
        b = curveParams.getBigInteger("b");
    }


    protected void initFields() {
        // Init Zr
        Zr = initFp(r);

        // Init Fq
        Fq = initFp(q);

        // Init Eq
        Eq = initEq(Fq.newElement().set(a),
                    Fq.newElement().set(b),
                    r, h);

        // Init Fqx
        PolyField polyField = initPoly();
        Fqx = polyField;

        // Init the irreducible polynomial
        int d = k / 2;

        PolyElement irreduciblePoly = polyField.newElement();
        List<Element> irreduciblePolyCoeff = irreduciblePoly.getCoefficients();
        for (int i = 0; i < d; i++) {
            irreduciblePolyCoeff.add(polyField.getTargetField().newElement().set(curveParams.getBigInteger("coeff" + i)));
        }
        irreduciblePolyCoeff.add(polyField.getTargetField().newElement().setToOne());

        // init Fqd
        Fqd  = initPolyMod(irreduciblePoly);

        // init Fqk
        Fqk = initQuadratic();

        if (k == 6) {
            phikonr = q.multiply(q).subtract(q).add(BigInteger.ONE).divide(r);

            PolyModElement polyModElement = Fqd.newElement();
            polyModElement.getCoefficient(1).setToOne();

            polyModElement.pow(q);

            xpowq = polyModElement;
            xpowq2 = polyModElement.duplicate().square();

            /*
            mpz_ptr q = param->q;
            mpz_ptr z = pairing->phikonr;
            mpz_init(z);
            mpz_mul(z, q, q);
            mpz_sub(z, z, q);
            mpz_add_ui(z, z, 1);
            mpz_divexact(z, z, pairing->r);

            element_ptr e = p->xpowq;
            element_init(e, p->Fqd);
            element_set1(((element_t *) e->data)[1]);
            element_pow_mpz(e, e, q);

            element_init(p->xpowq2, p->Fqd);
            element_square(p->xpowq2, e);
            */
        } else {
            tateexp = Fqk.getOrder().subtract(BigInteger.ONE).divide(r);
            /*
            mpz_init(p - > tateexp);
            mpz_sub_ui(p - > tateexp, p - > Fqk - > order, 1);
            mpz_divexact(p - > tateexp, p - > tateexp, pairing - > r);
            */
        }

        // init Etwist
        Etwist = initEqMap().twist();

        nqrInverse = Fqd.getNqr().duplicate().invert();
        nqrInverseSquare = nqrInverse.duplicate().square();

        // Init G1, G2, GT
        G1 = Eq;
        G2 = Etwist;
        GT = initGT();

/*
        cc_miller_no_denom_fn = cc_miller_no_denom_affine;
        pairing->option_set = d_pairing_option_set;
        pairing->pp_init = d_pairing_pp_init;
        pairing->pp_clear = d_pairing_pp_clear;
        pairing->pp_apply = d_pairing_pp_apply;

        pairing->clear_func = d_pairing_clear;

        */
    }

    protected Field initFp(BigInteger order) {
        return new NaiveField(order);
    }

    protected CurveField initEq(Element a, Element b, BigInteger order, BigInteger cofac) {
        return new CurveField(a, b, order, cofac);
    }

    protected CurveField initEqMap() {
        Element a = Fqd.newElement().map(Eq.getA());
        Element b = Fqd.newElement().map(Eq.getB());

        return initEq(a, b, r, null);
    }

    protected PolyField initPoly() {
        return new PolyField(Fq);
    }

    protected PolyModField initPolyMod(PolyElement irred) {
        return new PolyModField(irred, curveParams.getBigInteger("nqr"));
    }

    protected QuadraticEvenField initQuadratic() {
        return new QuadraticEvenField(Fqd);
    }

    protected Field initGT() {
        return new GTFiniteField(pairingMap, Fqk);
    }

    protected void initMap() {
        pairingMap = new CCPairingMap(this);
    }



}