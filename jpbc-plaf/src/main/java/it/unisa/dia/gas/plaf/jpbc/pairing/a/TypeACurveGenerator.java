package it.unisa.dia.gas.plaf.jpbc.pairing.a;

import it.unisa.dia.gas.jpbc.CurveGenerator;
import it.unisa.dia.gas.plaf.jpbc.util.BigIntegerUtils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TypeACurveGenerator implements CurveGenerator {
    protected int rbits, qbits;


    public TypeACurveGenerator(int rbits, int qbits) {
        this.rbits = rbits;
        this.qbits = qbits;
    }

    public Map generate() {
        Map params = new LinkedHashMap();

        boolean found = false;

        BigInteger q;
        BigInteger r;
        BigInteger h = null;
        int exp1, exp2;
        int sign0, sign1;

        SecureRandom random = new SecureRandom();
        do {
            int i;

            r = BigInteger.ZERO;

            if (random.nextInt(Integer.MAX_VALUE) % 2 != 0) {
                exp2 = rbits - 1;
                sign1 = 1;
            } else {
                exp2 = rbits;
                sign1 = -1;
            }
            r = r.setBit(exp2);

            //use q as a temp variable
            q = BigInteger.ZERO;
            exp1 = (random.nextInt(Integer.MAX_VALUE) % (exp2 - 1)) + 1;
            q = q.setBit(exp1);

            if (sign1 > 0) {
                r = r.add(q);
            } else {
                r = r.subtract(q);
            }

            if (random.nextInt(Integer.MAX_VALUE) % 2 != 0) {
                sign0 = 1;
                r = r.add(BigInteger.ONE);
            } else {
                sign0 = -1;
                r = r.subtract(BigInteger.ONE);
            }

            if (!r.isProbablePrime(10))
                continue;

            for (i = 0; i < 10; i++) {
                int bit;

                //use q as a temp variable
                q = BigInteger.ZERO;
                bit = qbits - rbits - 4 + 1;
                if (bit < 3)
                    bit = 3;
                q = q.setBit(bit);

                h = BigIntegerUtils.getRandom(q);
                h = h.multiply(BigIntegerUtils.TWELVE);

                //finally q takes the value it should
                q = h.multiply(r);
                q = q.subtract(BigInteger.ONE);

                if (q.isProbablePrime(10)) {
                    found = true;
                    break;
                }
            }
        } while (!found);

        params.put("type", "a");
        params.put("q", q.toString());
        params.put("h", h.toString());
        params.put("r", r.toString());
        params.put("exp2", "" + exp2);
        params.put("exp1", "" + exp1);
        params.put("sign1", "" + sign1);
        params.put("sign0", "" + sign0);

        return params;
    }

    public static void main(String[] args) {
        int rbits = 181, qbits = 603;

        Map params = new TypeACurveGenerator(rbits, qbits).generate();
        for (Object key : params.keySet()) {
            System.out.println(key + "=" + params.get(key));
        }



    }
}
