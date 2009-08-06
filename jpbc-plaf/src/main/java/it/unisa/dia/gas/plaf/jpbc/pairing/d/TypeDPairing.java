package it.unisa.dia.gas.plaf.jpbc.pairing.d;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Point;
import it.unisa.dia.gas.jpbc.Polynomial;
import it.unisa.dia.gas.plaf.jpbc.field.curve.CurveField;
import it.unisa.dia.gas.plaf.jpbc.field.gt.GTFiniteField;
import it.unisa.dia.gas.plaf.jpbc.field.naive.NaiveField;
import it.unisa.dia.gas.plaf.jpbc.field.poly.PolyElement;
import it.unisa.dia.gas.plaf.jpbc.field.poly.PolyField;
import it.unisa.dia.gas.plaf.jpbc.field.polymod.PolyModElement;
import it.unisa.dia.gas.plaf.jpbc.field.polymod.PolyModField;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticField;
import it.unisa.dia.gas.plaf.jpbc.pairing.AbstractPairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;

import java.math.BigInteger;
import java.util.List;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TypeDPairing extends AbstractPairing {
    protected CurveParams curveParams;

    protected int k;

    protected BigInteger r;
    protected BigInteger q;
    protected BigInteger h;
    protected BigInteger a;
    protected BigInteger b;

    protected PolyModElement xpowq, xpowq2;
    protected Element nqrInverse, nqrInverseSquare;
    protected BigInteger tateExp;

    protected BigInteger phikonr;

    protected Field Fq, Fqx;
    protected Field<? extends Point<Polynomial>> Fqk;
    protected PolyModField Fqd;
    protected CurveField Eq, Etwist;


    public TypeDPairing(CurveParams curveParams) {
        this.curveParams = curveParams;

        initParams();
        initMap();
        initFields();
    }


    public boolean isSymmetric() {
        return false;
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

        if (k == 6) {
            phikonr = q.multiply(q).subtract(q).add(BigInteger.ONE).divide(r);

            PolyModElement polyModElement = Fqd.newElement();
            polyModElement.getCoefficient(1).setToOne();

            polyModElement.pow(q);

            xpowq = polyModElement;
            xpowq2 = polyModElement.duplicate().square();
        } else {
            tateExp = Fqk.getOrder().subtract(BigInteger.ONE).divide(r);
        }

        // init Etwist
        Etwist = initEqMap().twist();

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
        return new CurveField(Fq.newElement().set(a),Fq.newElement().set(b), r, h);
    }

    protected CurveField initEqMap() {
        return new CurveField(Fqd.newElement().map(Eq.getA()),    Fqd.newElement().map(Eq.getB()), r, null);
    }

    protected PolyField initPoly() {
        return new PolyField(Fq);
    }

    protected PolyModField initPolyMod(PolyElement irred) {
        return new PolyModField(irred, curveParams.getBigInteger("nqr"));
    }

    protected QuadraticField initQuadratic() {
        return new QuadraticField(Fqd);
    }

    protected Field initGT() {
        return new GTFiniteField(pairingMap, Fqk);
    }

    protected void initMap() {
        pairingMap = new TypeDMillerNoDenomAffinePairingMap(this);
    }

}