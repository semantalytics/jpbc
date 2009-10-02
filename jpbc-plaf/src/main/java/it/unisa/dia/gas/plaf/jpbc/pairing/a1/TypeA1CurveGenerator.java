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

    public TypeA1CurveGenerator() {
    }

    public Map generate() {
        SecureRandom secureRandom = new SecureRandom();

        BigInteger p = new BigInteger(512, 16, secureRandom);
        BigInteger q = new BigInteger(512, 16, secureRandom);
        BigInteger order = p.multiply(q);

        // If order is even, ideally check all even l, not just multiples of 4
        long l = 4;
        BigInteger n = order.multiply(BigIntegerUtils.FOUR);
        p = n.subtract(BigInteger.ONE);
        while (!p.isProbablePrime(20)){
            p = p.add(n);
            l += 4;
        }

        CurveParams params = new CurveParams();
        params.put("type", "a1");
        params.put("p", p.toString());
        params.put("n", order.toString());
        params.put("l", String.valueOf(l));

        return params;
    }

    public static void main(String[] args) {
        TypeA1CurveGenerator generator = new TypeA1CurveGenerator();
        CurveParams curveParams = (CurveParams) generator.generate();

        System.out.println(curveParams.toString(" "));
    }

}
