package it.unisa.dia.gas.plaf.jpbc.pairing.a1;

import it.unisa.dia.gas.jpbc.CurveGenerator;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.field.curve.CurveField;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.util.BigIntegerUtils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Map;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TypeA1CurveGenerator implements CurveGenerator {
    protected int n, bits;


    public TypeA1CurveGenerator(int n, int bits) {
        this.n = n;
        this.bits = bits;
    }


    public Map generate() {
        SecureRandom secureRandom = new SecureRandom();

        BigInteger[] primes = new BigInteger[n];
        BigInteger order = BigInteger.ONE;
        for (int i = 0; i < n; i++) {
            primes[i] = BigIntegerUtils.generateSolinasPrime(bits, secureRandom);
            order = order.multiply(primes[i]);
        }

        // If order is even, ideally check all even l, not just multiples of 4
        long l = 4;
        BigInteger n = order.multiply(BigIntegerUtils.FOUR);

        BigInteger p = n.subtract(BigInteger.ONE);
        while (!p.isProbablePrime(20)){
            p = p.add(n);
            l += 4;
        }

        CurveParams params = new CurveParams();
        params.put("type", "a1");
        params.put("p", p.toString());
        for (int i = 0; i < primes.length; i++) {
            params.put("p"+ i, primes[i].toString());

        }
        params.put("n", order.toString());
        params.put("l", String.valueOf(l));

        return params;
    }

    public static void main(String[] args) {
        TypeA1CurveGenerator generator = new TypeA1CurveGenerator(3, 128);
        CurveParams curveParams = (CurveParams) generator.generate();

        System.out.println(curveParams.toString(" "));


        Pairing pairing = new TypeA1Pairing(curveParams);

        Element e1 = ((CurveField)pairing.getG1()).getGen().duplicate();
        Element e2 = ((CurveField)pairing.getG1()).getGen().duplicate();

        System.out.println(pairing.pairing(
                e1.pow(curveParams.getBigInteger("p0").multiply(curveParams.getBigInteger("p2"))),
                e2.pow(curveParams.getBigInteger("p1").multiply(curveParams.getBigInteger("p2")))));
    }

}
