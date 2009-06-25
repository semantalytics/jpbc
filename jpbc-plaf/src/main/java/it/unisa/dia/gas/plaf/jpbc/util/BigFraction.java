package it.unisa.dia.gas.plaf.jpbc.util;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class BigFraction {
    BigInteger numerator;
    BigInteger denominator;

    public BigFraction(int n) {
        numerator = BigInteger.valueOf(n);
        denominator = BigInteger.ONE;
    }

    public BigFraction(int n, int d) {
        numerator = BigInteger.valueOf(n);
        denominator = BigInteger.valueOf(d);
    }

    public BigFraction(BigInteger n, BigInteger d) {
        numerator = new BigInteger(n.toByteArray());
        denominator = new BigInteger(d.toByteArray());
    }

    public BigFraction(BigFraction f) {
        this(f.numerator, f.denominator);
    }

    public BigInteger getNominator() {
        reduceThis();
        return numerator;
    }

    public BigInteger getDenominator() {
        reduceThis();
        return denominator;
    }

    public BigFraction add(BigFraction rhs) {
        // a   b      a.d + b.c
        // - + -  =   ---------
        // c   d         c.d
        // a/c = this
        // b/d = rhs
        BigInteger ad = numerator.multiply(rhs.denominator);
        BigInteger bc = denominator.multiply(rhs.numerator);
        BigInteger cd = denominator.multiply(rhs.denominator);
        return new BigFraction(ad.add(bc), cd);
    }

    public BigFraction subtract(BigFraction rhs) {
        BigInteger ad = numerator.multiply(rhs.denominator);
        BigInteger bc = denominator.multiply(rhs.numerator);
        BigInteger cd = denominator.multiply(rhs.denominator);
        return new BigFraction(ad.subtract(bc), cd);
    }

    public BigFraction multiply(BigFraction rhs) {
        return new BigFraction(numerator.multiply(rhs.numerator), denominator.multiply(rhs.denominator));
    }

    public BigFraction divide(BigFraction rhs) {
        // we turn the fraction upside down and multiply
        return new BigFraction(numerator.multiply(rhs.denominator), denominator.multiply(rhs.numerator));
    }

    public BigFraction min(BigFraction rhs) {
        return compareTo(rhs) < 0 ? new BigFraction(this) : new BigFraction(rhs);
    }

    public BigFraction max(BigFraction rhs) {
        return compareTo(rhs) > 0 ? new BigFraction(this) : new BigFraction(rhs);
    }

    public BigFraction abs() {
        return new BigFraction(numerator.abs(), denominator.abs());
    }

    public BigFraction reciprocal() {
        return new BigFraction(denominator, numerator);
    }

    public BigFraction negate() {
        reduceThis();
        return new BigFraction(numerator.negate(), denominator);
    }

    public int compareTo(BigFraction rhs) {
        // There's no LCM in BigInteger, so we get identical denominators
        // by multipling them together.

        // We're not reducing since that appeared to be more
        // costly than just doing the multiplication on (potentially) larger
        // numbers.

        BigInteger lhsNominator = numerator.multiply(rhs.denominator);
        BigInteger rhsNominator = denominator.multiply(rhs.numerator);
        // the denomintor for both fractions is denom*rhs.denom, but we
        // don't need to compute that

        return lhsNominator.compareTo(rhsNominator);
    }

    public boolean equals(BigFraction rhs) {
        return compareTo(rhs) == 0 ? true : false;
    }

    public static String toString(BigFraction f) {
        return f.toString();
    }

    public String toString() {
        reduceThis();
        String str = "";

        // Is it negative? The reduction ensures only the nom is -ve, and is handled
        // by either:
        // 1. the whole number
        // 2. the nom in the purely fractional version
        // Therefore we need to abs dar[1] later

        // Is the fraction top-heavy? If so, we need to introduce a whole number
        BigInteger[] dar = numerator.divideAndRemainder(denominator);

        if (!dar[0].equals(BigInteger.ZERO)) {
            str += dar[0].toString() + " ";
            // Calculate the new nominator; which not coincidentally
            // is the remainder
            str += dar[1].abs().toString();
        } else {
            str += numerator.toString();
        }

        return str + "/" + denominator.toString();
    }

    public int intValue() {
        reduceThis();
        return numerator.divide(denominator).intValue();
    }

    public long longValue() {
        reduceThis();
        return numerator.divide(denominator).longValue();
    }

    public float floatValue() {
        // TODO When we get BigFraction we can convert numerator
        // and denominator into that, before calculating the
        // division, and returning the result.
        reduceThis();
        return numerator.floatValue() / denominator.floatValue();
    }

    public double doubleValue() {
        // TODO See note in @floatValue
        reduceThis();
        return numerator.doubleValue() / denominator.doubleValue();
    }

    public BigFraction reduce() {
        BigInteger newNumerator, newDenominator;

        if (numerator.signum() == denominator.signum()) {
            newNumerator = numerator.abs();
            newDenominator = denominator.abs();
        } else {
            newNumerator = numerator.abs().negate();
            newDenominator = denominator.abs();
        }
        BigInteger gdc = newNumerator.gcd(newDenominator);

        if (BigInteger.ONE.equals(gdc))
            return new BigFraction(newNumerator, newDenominator);

        return new BigFraction(newNumerator.divide(gdc), newDenominator.divide(gdc)).reduce();
    }

    public void reduceThis() {
        BigFraction reduced = reduce();

        numerator = reduced.numerator;
        denominator = reduced.denominator;
    }

    public BigFraction inverse() {
        return new BigFraction(denominator, numerator);
    }
}
