package it.unisa.dia.gas.plaf.jpbc.pairing.e;

import it.unisa.dia.gas.jpbc.CurveGenerator;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.field.curve.CurveField;
import it.unisa.dia.gas.plaf.jpbc.field.naive.NaiveField;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.util.BigIntegerUtils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Map;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TypeECurveGenerator implements CurveGenerator {
    protected int rBits, qBits;


    public TypeECurveGenerator(int rBits, int qBits) {
        this.rBits = rBits;
        this.qBits = qBits;
    }

    
    public Map generate() {
        SecureRandom secureRandom = new SecureRandom();

        //3 takes 2 bits to represent
        BigInteger q;
        BigInteger r;
        BigInteger h = null;
        BigInteger n = null;

        //won't find any curves is hBits is too low
        int hBits = (qBits - 2) / 2 - rBits;
        if (hBits < 3)
            hBits = 3;

        int exp2;
        int exp1;
        int sign0, sign1;

        boolean found = false;
        do {
            r = BigInteger.ZERO;

            if (secureRandom.nextInt(Integer.MAX_VALUE) % 2 != 0) {
                exp2 = rBits - 1;
                sign1 = 1;
            } else {
                exp2 = rBits;
                sign1 = -1;
            }
            r = r.setBit(exp2);

            exp1 = (secureRandom.nextInt(Integer.MAX_VALUE) % (exp2 - 1)) + 1;

            //use q as a temp variable
            q = BigInteger.ZERO;
            q = q.setBit(exp1);

            if (sign1 > 0) {
                r = r.add(q);
            } else {
                r = r.subtract(q);
            }

            if (secureRandom.nextInt(Integer.MAX_VALUE) % 2 != 0) {
                sign0 = 1;
                r = r.add(BigInteger.ONE);
            } else {
                sign0 = -1;
                r = r.subtract(BigInteger.ONE);
            }
            if (!r.isProbablePrime(10))
                continue;

            for (int i = 0; i < 10; i++) {
                //use q as a temp variable
                q = BigInteger.ZERO;
                q = q.setBit(hBits + 1);

                h = BigIntegerUtils.getRandom(q);
                h = h.multiply(h).multiply(BigIntegerUtils.THREE);

                //finally q takes the value it should
                n = r.multiply(r).multiply(h).add(BigInteger.ONE);
                if (q.isProbablePrime(10)) {
                    found = true;
                    break;
                }
            }
        } while (!found);

        Field Fq = new NaiveField(q);
        CurveField curveField = new CurveField(Fq.newZeroElement(), Fq.newOneElement(), n, null);

        // We may need to twist it.
        // Pick a random point P and twist the curve if P has the wrong order.
        Element P = curveField.newElement().setToRandom().mul(n);
        if (!P.isZero())
            curveField.reinitCurveTwist();

        CurveParams params = new CurveParams();
        params.put("q", q.toString());
        params.put("r", r.toString());
        params.put("h", h.toString());
        params.put("exp1", String.valueOf(exp1));
        params.put("exp2", String.valueOf(exp2));
        params.put("sign0", String.valueOf(sign0));
        params.put("sign1", String.valueOf(sign1));
        params.put("a", curveField.getA().toBigInteger().toString());
        params.put("b", curveField.getB().toBigInteger().toString());

        return params;
    }
}