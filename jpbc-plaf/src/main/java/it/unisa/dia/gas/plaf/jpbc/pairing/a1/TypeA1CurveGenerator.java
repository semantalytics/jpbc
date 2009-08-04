package it.unisa.dia.gas.plaf.jpbc.pairing.a1;

import it.unisa.dia.gas.jpbc.CurveGenerator;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.util.BigIntegerUtils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Map;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TypeA1CurveGenerator implements CurveGenerator {
    protected int rBits, qBits;

    public TypeA1CurveGenerator(int rBits, int qBits) {
        this.rBits = rBits;
        this.qBits = qBits;
    }

    public Map generate() {
        SecureRandom secureRandom = new SecureRandom();

        BigInteger q = new BigInteger(qBits, 16, secureRandom);
        BigInteger r = new BigInteger(rBits, 16, secureRandom);
        BigInteger N = q.multiply(r);

        // If order is even, ideally check all even l, not just multiples of 4
        long l = 4;
        BigInteger n = N.multiply(BigIntegerUtils.FOUR);
        BigInteger p = n.subtract(BigInteger.ONE);
        while (!p.isProbablePrime(20)){
            p = p.add(n);
            l += 4;
        }

        CurveParams params = new CurveParams();
        params.put("p", p.toString());
        params.put("n", n.toString());
        params.put("l", String.valueOf(l));

        return params;
    }
}
