package it.unisa.dia.gas.plaf.jpbc.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class BigDecimalUtils {
    public static final BigDecimal TWO = BigDecimal.valueOf(2);


    public static BigDecimal compute_pi(int precision) {
        MathContext mathContext = new MathContext(precision);

        BigInteger k1 = new BigInteger("545140134");
        BigInteger k2 = new BigInteger("13591409");
        BigInteger k4 = new BigInteger("100100025");
        BigInteger k5 = new BigInteger("327843840");

        int k3 = 640320;
        int k6 = 53360;

        BigInteger d = k4.max(k5);
        d = d.multiply(BigIntegerUtils.EIGHT);
        BigFraction p = new BigFraction(BigInteger.ZERO, BigInteger.ONE);
        BigFraction q;


        int nlimit = precision / 47 + 1;
        boolean toggle = true;
        for (int n = 0; n < nlimit; n++) {
            BigInteger z0 = BigIntegerUtils.factorial(6 * n);
            BigInteger z1 = k1.multiply(BigInteger.valueOf(n));
            z1 = z1.add(k2);
            z0 = z0.multiply(z1);

            z1 = BigIntegerUtils.factorial(3 * n);
            BigInteger z2 = BigIntegerUtils.factorial(n);
            z2 = z2.pow(3);
            z1 = z1.multiply(z2);
            z2 = d.pow(n);
            z1 = z1.multiply(z2);

            q = new BigFraction(z0, z1);
            q.reduceThis();

            if (toggle) {
                p = p.add(q);
            } else {
                p = p.subtract(q);
            }
            toggle = !toggle;
        }

        q = p.inverse();
        q = new BigFraction(
                q.getNominator().multiply(BigInteger.valueOf(k6)),
                q.getDenominator()
        );
        q.reduceThis();

        BigDecimal pi = new BigDecimal(q.getNominator(), mathContext).divide(new BigDecimal(q.getDenominator(), mathContext), mathContext);
        BigDecimal f1 = new BigDecimal(Math.sqrt(k3), mathContext);
        pi = pi.multiply(f1);

        return pi;
    }

    
 

    public static void main(String[] args) {
        System.out.println(compute_pi(100));
    }
    
}
