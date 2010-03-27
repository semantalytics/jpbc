package it.unisa.dia.gas.plaf.jpbc.pairing.a1;

import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Point;
import it.unisa.dia.gas.plaf.jpbc.field.curve.CurveField;
import it.unisa.dia.gas.plaf.jpbc.field.gt.GTFiniteField;
import it.unisa.dia.gas.plaf.jpbc.field.naive.NaiveField;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.DegreeTwoQuadraticField;
import it.unisa.dia.gas.plaf.jpbc.pairing.AbstractPairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TypeA1Pairing extends AbstractPairing {
    protected BigInteger r;
    protected BigInteger p;
    protected long l;

    protected BigInteger phikonr;

    protected Field Fp;
    protected Field<? extends Point> Fq2;
    protected Field<? extends Point> Eq;


    public TypeA1Pairing(CurveParams properties) {
        initParams(properties);
        initMap();
        initFields();
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

        //k=2, hence phi_k(q) = q + 1, phikOnr = (q+1)/r
        phikonr = BigInteger.valueOf(l);

        // Init Fp
        Fp = initFp(p);

        // Init Eq
        Eq = initEq();

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

    protected Field<? extends Point> initEq() {
        return new CurveField<Field>(Fp.newOneElement(), Fp.newZeroElement(), r, phikonr);
    }

    protected Field<? extends Point> initFi() {
        return new DegreeTwoQuadraticField<Field>(Fp);
    }

    protected Field initGT(Field field) {
        return new GTFiniteField(r, pairingMap, field);
    }

    protected void initMap() {
        pairingMap = new TypeA1MillerAffinePairingMap(this);
    }

}