package it.unisa.dia.gas.plaf.jpbc.pairing.d;

import it.unisa.dia.gas.jpbc.*;
import it.unisa.dia.gas.plaf.jpbc.field.curve.CurveField;
import it.unisa.dia.gas.plaf.jpbc.field.gt.GTFiniteField;
import it.unisa.dia.gas.plaf.jpbc.field.naive.NaiveField;
import it.unisa.dia.gas.plaf.jpbc.field.poly.PolyElement;
import it.unisa.dia.gas.plaf.jpbc.field.poly.PolyField;
import it.unisa.dia.gas.plaf.jpbc.field.polymod.PolyModElement;
import it.unisa.dia.gas.plaf.jpbc.field.polymod.PolyModField;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticField;
import it.unisa.dia.gas.plaf.jpbc.pairing.AbstractPairing;
import it.unisa.dia.gas.plaf.jpbc.util.BigIntegerUtils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TypeDPairing extends AbstractPairing {
    protected CurveParameters curveParams;

    protected int k;

    protected BigInteger q, n, r, h;
    protected BigInteger a, b;

    protected PolyModElement xPowq, xPowq2;
    protected Element nqrInverse, nqrInverseSquare;
    protected BigInteger tateExp, phikOnr;

    protected Field Fq, Fqx;
    protected Field<? extends Point<Polynomial>> Fqk;
    protected PolyModField Fqd;
    protected CurveField Eq, Etwist;


    public TypeDPairing(Random random, CurveParameters curveParams) {
        super(random);

        this.curveParams = curveParams;

        initParams();
        initMap();
        initFields();
    }

    public TypeDPairing(CurveParameters curveParams) {
        this(new SecureRandom(), curveParams);
    }


    public boolean isSymmetric() {
        return false;
    }


    protected void initParams() {
        // validate the type
        String type = curveParams.getString("type");
        if (type == null || !"d".equalsIgnoreCase(type))
            throw new IllegalArgumentException("Type not valid. Found '" + type + "'. Expected 'd'.");

        // load params
        k = curveParams.getInt("k");
        if (k % 2 != 0)
            throw new IllegalArgumentException("odd k not implemented anymore");

        r = curveParams.getBigInteger("r");
        q = curveParams.getBigInteger("q");
        h = curveParams.getBigInteger("h");
        n = curveParams.getBigInteger("n");

        a = curveParams.getBigInteger("a");
        b = curveParams.getBigInteger("b");
    }


    protected void initFields() {
        // Init Zr
        Zr = initFp(r);

        // Init Fq
        Fq = initFp(q);

        // Init Eq
        Eq = initEq();

        // Init Fqx
        PolyField polyField = initPoly();
        Fqx = polyField;

        // Init the irreducible polynomial
        int d = k / 2;

        PolyElement<Element> irreduciblePoly = polyField.newElement();
        List<Element> irreduciblePolyCoeff = irreduciblePoly.getCoefficients();
        for (int i = 0; i < d; i++) {
            irreduciblePolyCoeff.add(polyField.getTargetField().newElement().set(curveParams.getBigInteger("coeff" + i)));
        }
        irreduciblePolyCoeff.add(polyField.getTargetField().newElement().setToOne());

        // init Fqd
        Fqd  = initPolyMod(irreduciblePoly);

        // init Fqk
        Fqk = initQuadratic();

        // Compute constants involved in the final powering.
        if (k == 6) {
            phikOnr = q.multiply(q).subtract(q).add(BigInteger.ONE).divide(r);

            PolyModElement polyModElement = Fqd.newElement();
            polyModElement.getCoefficient(1).setToOne();

            polyModElement.pow(q);

            xPowq = polyModElement;
            xPowq2 = polyModElement.duplicate().square();
        } else {
            tateExp = Fqk.getOrder().subtract(BigInteger.ONE).divide(r);
        }

        // init etwist
        Etwist = initEqMap().twist();

        // ndonr temporarily holds the trace.
        BigInteger ndonr = q.subtract(n).add(BigInteger.ONE) ;

        // Negate it because we want the trace of the twist.
        ndonr = ndonr.negate();
        ndonr = BigIntegerUtils.pbc_mpz_curve_order_extn(q, ndonr, d);
        ndonr = ndonr.divide(r);
        Etwist.setQuotientCmp(ndonr);

        nqrInverse = Fqd.getNqr().duplicate().invert();
        nqrInverseSquare = nqrInverse.duplicate().square();

        // Init G1, G2, GT
        G1 = Eq;
        G2 = Etwist;
        GT = initGT();
   }

    protected Field initFp(BigInteger order) {
        return new NaiveField(order);
    }

    protected CurveField initEq() {
        return new CurveField(random, Fq.newElement().set(a),Fq.newElement().set(b), r, h);
    }

    protected CurveField initEqMap() {
        return new CurveField(random, Fqd.newElement().map(Eq.getA()), Fqd.newElement().map(Eq.getB()), r, null);
    }

    protected PolyField initPoly() {
        return new PolyField(random, Fq);
    }

    protected PolyModField initPolyMod(PolyElement irred) {
        return new PolyModField(random, irred, curveParams.getBigInteger("nqr"));
    }

    protected QuadraticField initQuadratic() {
        return new QuadraticField(random, Fqd);
    }

    protected Field initGT() {
        return new GTFiniteField(random, r, pairingMap, Fqk);
    }

    protected void initMap() {
        pairingMap = new TypeDTateAffineNoDenomMillerPairingMap(this);
    }

}