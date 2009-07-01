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

/*        // Chudnovsky brothers' Ramanujan formula
        // http://www.cs.uwaterloo.ca/~alopez-o/math-faq/mathtext/node12.html

        mpz_t k1, k2, k4, k5, d;
        unsigned int k3 = 640320;
        unsigned int k6 = 53360;
        mpz_t z0, z1, z2;
        mpq_t p, q;
        mpf_t f1;
        int toggle = 1;
        int n;
        //converges fast: each term gives over 47 bits
        int nlimit = prec / 47 + 1;

        mpz_init(k1);
        mpz_init(k2);
        mpz_init(k4);
        mpz_init(k5);
        mpz_init(d);
        mpz_init(z0);
        mpz_init(z1);
        mpz_init(z2);
        mpq_init(q);
        mpq_init(p);
        mpf_init(f1);

        mpz_set_str(k1, "545140134", 10);
        mpz_set_str(k2, "13591409", 10);
        mpz_set_str(k4, "100100025", 10);
        mpz_set_str(k5, "327843840", 10);

        mpz_mul(d, k4, k5);
        mpz_mul_2exp(d, d, 3);
        mpq_set_ui(p, 0, 1);

        for (n = 0; n < nlimit; n++) {
            mpz_fac_ui(z0, 6 * n);
            mpz_mul_ui(z1, k1, n);
            mpz_add(z1, z1, k2);
            mpz_mul(z0, z0, z1);

            mpz_fac_ui(z1, 3 * n);
            mpz_fac_ui(z2, n);
            mpz_pow_ui(z2, z2, 3);
            mpz_mul(z1, z1, z2);
            mpz_pow_ui(z2, d, n);
            mpz_mul(z1, z1, z2);

            mpz_set(mpq_numref(q), z0);
            mpz_set(mpq_denref(q), z1);
            mpq_canonicalize(q);
            if (toggle) {
                mpq_add(p, p, q);
            } else {
                mpq_sub(p, p, q);
            }
            toggle = !toggle;
        }

        mpq_inv(q, p);
        mpz_mul_ui(mpq_numref(q), mpq_numref(q), k6);
        mpq_canonicalize(q);
        mpf_set_q(pi, q);
        mpf_sqrt_ui(f1, k3);
        mpf_mul(pi, pi, f1);

        //mpf_out_str(stdout, 0, 14 * nlimit, pi);
        //printf("\n");*/
        //throw new IllegalStateException("Not Implemented yet!!!");
    }


    public static void main(String[] args) {
        System.out.println(compute_pi(10));
    }
    
}
