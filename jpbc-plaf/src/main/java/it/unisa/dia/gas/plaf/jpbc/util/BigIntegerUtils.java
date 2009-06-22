package it.unisa.dia.gas.plaf.jpbc.util;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class BigIntegerUtils {
    public static final BigInteger TWO = new BigInteger("2");
    public static final BigInteger THREE = new BigInteger("3");
    public static final BigInteger FOUR = new BigInteger("4");
    public static final BigInteger FIVE = new BigInteger("5");
    public static final BigInteger SEVEN = new BigInteger("7");
    public static final BigInteger EIGHT = new BigInteger("8");


    public static boolean isOdd(BigInteger integer) {
        return false;
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

    public static int jacobi(BigInteger a, BigInteger n){
        /* Precondition: a, n >= 0; n is odd */
        int ans = 0;

        if (BigInteger.ZERO.equals(a))
            ans = (BigInteger.ONE.equals(n)) ? 1 : 0;
        else if (TWO.equals(a)) {
            BigInteger mod = n.mod(EIGHT);
            if (BigInteger.ONE.equals(mod) || SEVEN.equals(mod))
                ans = 1;
            else if (THREE.equals(mod) || FIVE.equals(mod))
                ans = -1;
        } else if (a.compareTo(n) >= 0)
            ans = jacobi(a.mod(n), n);
        else if (BigInteger.ZERO.equals(a.mod(TWO)))
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

}
