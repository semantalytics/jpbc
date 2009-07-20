package it.unisa.dia.gas.plaf.jpbc.util;

import java.math.BigInteger;
import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class BigIntegerUtils {
    public static final BigInteger TWO = BigInteger.valueOf(2);
    public static final BigInteger THREE = BigInteger.valueOf(3);
    public static final BigInteger FOUR = BigInteger.valueOf(4);
    public static final BigInteger FIVE = BigInteger.valueOf(5);
    public static final BigInteger SIX = BigInteger.valueOf(6);
    public static final BigInteger SEVEN = BigInteger.valueOf(7);
    public static final BigInteger EIGHT = BigInteger.valueOf(8);
    public static final BigInteger TWELVE = BigInteger.valueOf(12);
    public static final BigInteger MAXINT = BigInteger.valueOf(Integer.MAX_VALUE);
    public static final BigInteger ITERBETTER = ONE.shiftLeft(1024);


    public static boolean isOdd(BigInteger bigInteger) {
        return bigInteger.testBit(0);
    }

    public static BigInteger factorial(int n) {
        return factorial(BigInteger.valueOf(n));
    }

    public static BigInteger factorial(BigInteger n) {
        if (n.equals(ZERO))
            return ONE;

        BigInteger i = n.subtract(ONE);
        while (i.compareTo(ZERO) > 0) {
            n = n.multiply(i);
            i = i.subtract(ONE);
        }
        return n;
    }

    /**
     * Compute trace of Frobenius at q^n given trace at q
     * see p.105 of Blake, Seroussi and Smart
     *
     * @param q
     * @param trace
     * @param n
     * @return
     */
    public static BigInteger compute_trace_n(BigInteger q, BigInteger trace, int n) {
        int i;
        BigInteger c0, c1, c2;
        BigInteger t0;

        c2 = TWO;
        c1 = trace;

        for (i = 2; i <= n; i++) {

            c0 = trace.multiply(c1);
            t0 = q.multiply(c2);
            c0 = c0.subtract(t0);
            c2 = c1;
            c1 = c0;
        }

        return c1;
    }

    public static boolean isDivisible(BigInteger a, BigInteger b) {
        return a.remainder(b).compareTo(ZERO) == 0;
    }

    public static boolean isPerfectSquare(BigInteger n) {
        return fullSqrt(n)[1].signum() == 0;
    }

    public static BigInteger sqrt(BigInteger n) {
        return fullSqrt(n)[0];
    }

    /*	Compute the integer square root of n
        Precondition: n >= 0
        Postcondition: Result sr has the property sr[0]^2 <= n < (sr[0] + 1)^2 and (sr[0]^2 + sr[1] = n)
    */
    public static BigInteger[] fullSqrt(BigInteger n) {

        if (n.compareTo(MAXINT) < 1) {
            long ln = n.longValue();
            long s = (long) java.lang.Math.sqrt(ln);

            return new BigInteger[]{
                    BigInteger.valueOf(s),
                    BigInteger.valueOf(ln - s * s)
            };
        }

        BigInteger[] sr = isqrtInternal(n, n.bitLength() - 1);
        if (sr[1].signum() < 0) {
            return new BigInteger[]{
                    sr[0].subtract(ONE),
                    sr[1].add(sr[0].shiftLeft(1)).subtract(ONE)};
        }
        return sr;
    }

    /**
     * Calculate the Legendre symbol (a/p). This is defined only for p an odd positive prime,
     * and for such p it's identical to the Jacobi symbol.
     *
     * @param a
     * @param n
     * @return
     */
    public static int legendre(BigInteger a, BigInteger n) {
        return jacobi(a, n);
    }

    public static int jacobi(BigInteger a, BigInteger n) {
        /* Precondition: a, n >= 0; n is odd */
        int ans = 0;

        if (ZERO.equals(a))
            ans = (ONE.equals(n)) ? 1 : 0;
        else if (TWO.equals(a)) {
            BigInteger mod = n.mod(EIGHT);
            if (ONE.equals(mod) || SEVEN.equals(mod))
                ans = 1;
            else if (THREE.equals(mod) || FIVE.equals(mod))
                ans = -1;
        } else if (a.compareTo(n) >= 0)
            ans = jacobi(a.mod(n), n);
        else if (ZERO.equals(a.mod(TWO)))
            ans = jacobi(TWO, n) * jacobi(a.divide(TWO), n);
        else
            ans = (THREE.equals(a.mod(FOUR)) && THREE.equals(n.mod(FOUR))) ? -jacobi(n, a) : jacobi(n, a);

        return ans;
    }

    public static int scanOne(BigInteger a, int startIndex) {
        for (int i = startIndex, size = a.bitLength(); i < size; i++) {
            if (a.testBit(i))
                return i;
        }
        return -1;
    }

    public static BigInteger getRandom(BigInteger limit) {
        return getRandom(limit, new SecureRandom());
    }

    public static BigInteger getRandom(BigInteger limit, SecureRandom secureRandom) {
        BigInteger result;
        do {
            result = new BigInteger(limit.bitLength(), secureRandom);
        } while (limit.compareTo(result) >= 0);
        return result;
    }


    /*	Compute the integer square root of n or a number which is too large by one
        Precondition: n >= 0 and 2^log2n <= n < 2^(log2n + 1), i.e. log2n = floor(log2(n))
        Postcondition: Result sr has the property (sr[0]^2 - 1) <= n < (sr[0] + 1)^2 and (sr[0]^2 + sr[1] = n)
    */
    private static BigInteger[] isqrtInternal(BigInteger n, int log2n) {
        if (n.compareTo(MAXINT) < 1) {
            int ln = n.intValue(), s = (int) java.lang.Math.sqrt(ln);
            return new BigInteger[]{BigInteger.valueOf(s), BigInteger.valueOf(ln - s * s)};
        }
        if (n.compareTo(ITERBETTER) < 1) {
            int d = 7 * (log2n / 14 - 1), q = 7;
            BigInteger s = BigInteger.valueOf((long) java.lang.Math.sqrt(n.shiftRight(d << 1).intValue()));
            while (d > 0) {
                if (q > d) q = d;
                s = s.shiftLeft(q);
                d -= q;
                q <<= 1;
                s = s.add(n.shiftRight(d << 1).divide(s)).shiftRight(1);
            }
            return new BigInteger[]{s, n.subtract(s.multiply(s))};
        }
        int log2b = log2n >> 2;
        BigInteger mask = ONE.shiftLeft(log2b).subtract(ONE);
        BigInteger[] sr = isqrtInternal(n.shiftRight(log2b << 1), log2n - (log2b << 1));
        BigInteger s = sr[0];
        BigInteger[] qu = sr[1].shiftLeft(log2b).add(n.shiftRight(log2b).and(mask)).divideAndRemainder(s.shiftLeft(1));
        BigInteger q = qu[0];
        return new BigInteger[]{s.shiftLeft(log2b).add(q), qu[1].shiftLeft(log2b).add(n.and(mask)).subtract(q.multiply(q))};
    }


}
