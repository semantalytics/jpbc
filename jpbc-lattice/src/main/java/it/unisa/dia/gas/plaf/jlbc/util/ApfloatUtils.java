package it.unisa.dia.gas.plaf.jlbc.util;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;
import org.apfloat.Apint;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class ApfloatUtils {

    public final static int precision = 128;
    public final static int radix = 2;

    public final static Apfloat TWO = new Apfloat(2, precision, radix);
    public final static Apint ITWO = new Apint(2, radix);
    public final static Apfloat IONE = new Apint(1, radix);
    public static final Apfloat FIVE = new Apfloat(5, precision, radix);
    public static final Apfloat IFIVE = new Apint(5, radix);
    public static final Apint ITWELVE = new Apint(12, radix);
    public static final Apfloat ZERO = new Apfloat(0, precision, radix);;


    public static Apfloat newApfloat(int n) {
        return new Apfloat(n, precision, radix);
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
        return value.toRadix(10).toString(true);
    }

    public static Apfloat newApint(int value) {
        return new Apint(value, radix);
    }
}
