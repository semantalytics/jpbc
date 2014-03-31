package it.unisa.dia.gas.plaf.jlbc.util;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;
import org.apfloat.Apint;

import static org.apfloat.ApfloatMath.exp;
import static org.apfloat.ApfloatMath.pow;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class ApfloatUtils {

    public final static int precision = 160;
    public final static int radix = 2;


    public final static Apint IZERO = new Apint(0, radix);
    public final static Apint IONE = new Apint(1, radix);
    public final static Apint ITWO = new Apint(2, radix);
    public static final Apint IFOUR = new Apint(4, radix);
    public static final Apint IFIVE = new Apint(5, radix);
    public static final Apint ISIX = new Apint(6, radix);
    public static final Apint IEIGHT = new Apint(8, radix);
    public static final Apint ITWELVE = new Apint(12, radix);


    public final static Apfloat ONE = new Apfloat(1, precision, radix);
    public final static Apfloat TWO = new Apfloat(2, precision, radix);
    public static final Apfloat FIVE = new Apfloat(5, precision, radix);
    public static final Apfloat ZERO = new Apfloat(0, precision, radix);;


    public static Apfloat newApfloat(int n) {
        return new Apfloat(n, precision, radix);
    }

    public static Apint to_Apfloat(int n) {
        return new Apint(n, radix);
    }

    public static Apfloat sqrt(int n) {
        return ApfloatMath.sqrt(new Apfloat(n, precision, radix));
    }

    public static Apfloat pi() {
        return ApfloatMath.pi(precision, radix);
    }

    public static Apfloat square(Apfloat value) {
        return ApfloatMath.pow(value, 2);
    }

    public static String toString(Apfloat value) {
        if (value == null)
            return "null";
        return value.toRadix(10).toString(true);
    }

    public static Apfloat newApint(int value) {
        return new Apint(value, radix);
    }

    /**
     * computes e^{-1/2 (x/sigma)^2} *
     */
    public static Apfloat rho(Apfloat sigma, Apfloat x) {
//        return exp(-power(x/sigma, 2)/2.0);
        return exp(pow(x.divide(sigma), 2).negate().divide(TWO));
    }
}
