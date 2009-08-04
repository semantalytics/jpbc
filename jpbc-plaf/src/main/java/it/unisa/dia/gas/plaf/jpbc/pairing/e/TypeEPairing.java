package it.unisa.dia.gas.plaf.jpbc.pairing.e;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.Point;
import it.unisa.dia.gas.plaf.jpbc.field.curve.CurveField;
import it.unisa.dia.gas.plaf.jpbc.field.gt.GTFiniteField;
import it.unisa.dia.gas.plaf.jpbc.field.naive.NaiveField;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.map.PairingMap;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TypeEPairing implements Pairing {
    protected int exp2;
    protected int exp1;
    protected int sign1;
    protected int sign0;
    protected BigInteger r;
    protected BigInteger q;
    protected BigInteger h;
    protected BigInteger a;
    protected BigInteger b;

    protected BigInteger phikonr;
    protected Point R;

    protected Field Fq;
    protected Field<? extends Point> Eq;

    protected Field<? extends Point> G1, G2;
    protected Field GT, Zr;

    protected PairingMap pairingMap;


    public TypeEPairing(CurveParams properties) {
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

        pairingMap.initPairingPreProcessing((Point) g1);
    }

    public Element pairing(Element g2) {
        if (!G2.equals(g2.getField()))
            throw new IllegalArgumentException("pairing 2nd input mismatch");

        return pairingMap.pairing((Point) g2);
    }

    protected void initParams(CurveParams curveParams) {
        // validate the type
        String type = curveParams.get("type");
        if (type == null || !"e".equalsIgnoreCase(type))
            throw new IllegalArgumentException("Type not valid. Found '" + type + "'. Expected 'e'.");

        // load params
        exp2 = curveParams.getInt("exp2");
        exp1 = curveParams.getInt("exp1");
        sign1 = curveParams.getInt("sign1");
        sign0 = curveParams.getInt("sign0");

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
        CurveField<Field> Eq = initEq();
        this.Eq = Eq;

        // k=1, hence phikonr = (p-1)/r
        phikonr = Fq.getOrder().subtract(BigInteger.ONE).divide(r);

        // Init G1, G2, GT
        G1 = Eq;
        G2 = G1;
        GT = initGT();

        R = Eq.getGenNoCofac().duplicate();
    }


    protected Field initFp(BigInteger order) {
        return new NaiveField(order);
    }

    protected CurveField<Field> initEq() {
        return new CurveField<Field>(Fq.newElement().set(a),                                       Fq.newElement().set(b),
                                      r, h);
    }

    protected Field initGT() {
        return new GTFiniteField(pairingMap, Fq);
    }


    protected void initMap() {
        pairingMap = new EPairingMap(this);
    }
}