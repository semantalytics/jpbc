package it.unisa.dia.gas.plaf.jpbc.pairing.a;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.Point;
import it.unisa.dia.gas.plaf.jpbc.field.curve.CurveField;
import it.unisa.dia.gas.plaf.jpbc.field.gt.GTFiniteField;
import it.unisa.dia.gas.plaf.jpbc.field.naive.NaiveField;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticTwoField;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingMap;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TypeAPairing implements Pairing {
    protected int exp2;
    protected int exp1;
    protected int sign1;
    protected int sign0;
    protected BigInteger r; // r = 2^exp2 + sign1 * 2^exp1 + sign0 * 1
    protected BigInteger q; // we work in E(F_q) (and E(F_q^2))
    protected BigInteger h; // r * h = q + 1

    protected BigInteger phikonr;

    protected Field Fq;
    protected Field<? extends Point> Fq2;
    protected Field<? extends Point> Eq;

    protected Field<? extends Point> G1, G2;
    protected Field GT, Zr;

    protected PairingMap pairingMap;


    public TypeAPairing(CurveParams properties) {
        initParams(properties);
        initMap();
        initFields();
    }


    public boolean isSymmetric() {
        return true;
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

        return pairingMap.pairing((Point) g1, (Point) g2);
    }

    public void initPairingPreProcessing(Element g1) {
        if (!G1.equals(g1.getField()))
            throw new IllegalArgumentException("pairing 1st input mismatch");

        pairingMap.initPairingPreProcessing(g1);
    }

    public Element pairing(Element g2) {
        if (!G2.equals(g2.getField()))
            throw new IllegalArgumentException("pairing 2nd input mismatch");

        return pairingMap.pairing((Point) g2);
    }

    protected void initParams(CurveParams curveParams) {
        // validate the type
        String type = curveParams.get("type");
        if (type == null || !"a".equalsIgnoreCase(type))
            throw new IllegalArgumentException("Type not valid. Found '" + type + "'. Expected 'a'.");

        // load params
        exp2 = curveParams.getInt("exp2");
        exp1 = curveParams.getInt("exp1");
        sign1 = curveParams.getInt("sign1");
        sign0 = curveParams.getInt("sign0");

        r = curveParams.getBigInteger("r");
        q = curveParams.getBigInteger("q");
        h = curveParams.getBigInteger("h");
    }


    protected void initFields() {
        // Init Zr
        Zr = initFp(r);

        // Init Fq

        Fq = initFp(q);

        // Init Eq
        Eq = initEq(Fq.newOneElement(),
                    Fq.newZeroElement(),
                    r, h);

        // Init Fq2
        Fq2 = initFi();

        //k=2, hence phi_k(q) = q + 1, phikonr = (q+1)/r
        phikonr = h;

        // Init G1, G2, GT
        G1 = Eq;
        G2 = G1;
        GT = initGT(Fq2);
    }


    protected Field initFp(BigInteger order) {
        return new NaiveField(order);
    }

    protected Field<? extends Point> initEq(Element a, Element b, BigInteger order, BigInteger cofac) {
        return new CurveField<Field>(a, b, order, cofac);
    }

    protected Field<? extends Point> initFi() {
        return new QuadraticTwoField<Field>(Fq);
    }

    protected Field initGT(Field field) {
        return new GTFiniteField(pairingMap, field);
    }


    protected void initMap() {
        pairingMap = new ProjectionPairingMap(this);
    }
}
