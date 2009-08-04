package it.unisa.dia.gas.plaf.jpbc.pairing.a1;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.Point;
import it.unisa.dia.gas.plaf.jpbc.field.curve.CurveField;
import it.unisa.dia.gas.plaf.jpbc.field.gt.GTFiniteField;
import it.unisa.dia.gas.plaf.jpbc.field.naive.NaiveField;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.DegreeTwoQuadraticField;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.map.PairingMap;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TypeA1Pairing implements Pairing {
    protected BigInteger p;
    protected BigInteger r;
    protected long l;

    protected BigInteger phikonr;

    protected Field Fp;
    protected Field<? extends Point> Fq2;
    protected Field<? extends Point> Eq;

    protected Field<? extends Point> G1, G2;
    protected Field GT, Zr;

    protected PairingMap pairingMap;


    public TypeA1Pairing(CurveParams properties) {
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
        if (type == null || !"a1".equalsIgnoreCase(type))
            throw new IllegalArgumentException("Type not valid. Found '" + type + "'. Expected 'a1'.");

        // load params
        p = curveParams.getBigInteger("p");
        r = curveParams.getBigInteger("n");
        l = curveParams.getLong("l");
    }


    protected void initFields() {
        // Init Zr
        Zr = initFp(r);

        //k=2, hence phi_k(q) = q + 1, phikonr = (q+1)/r
        phikonr = BigInteger.valueOf(l);

        // Init Fp

        Fp = initFp(p);

        // Init Eq
        Eq = initEq(Fp.newOneElement(),
                    Fp.newZeroElement(),
                    r, phikonr);

        // Init Fq2
        Fq2 = initFi();

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
        return new DegreeTwoQuadraticField<Field>(Fp);
    }

    protected Field initGT(Field field) {
        return new GTFiniteField(pairingMap, field);
    }

    protected void initMap() {
        pairingMap = new TateMillerAffinePairingMap(this);
    }

    public static void main(String[] args) {
        System.out.println(new BigInteger("98826429041171753291515535532523512299028170537954154869719707264887274916552228805607584116490046284509883309001532457986879277885241872021906840932513241346999389365188296460009947").bitLength());
    }

}