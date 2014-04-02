package it.unisa.dia.gas.plaf.jlbc.field.floating;

import it.unisa.dia.gas.plaf.jpbc.field.base.AbstractField;
import org.apfloat.Apfloat;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class FloatingField extends AbstractField<FloatingElement> {

    protected int precision, radix;
    protected Apfloat zero, one;

    public FloatingField(SecureRandom random) {
        this(random, 128, 2);
    }

    public FloatingField(SecureRandom random, int precision, int radix) {
        super(random);

        this.precision = precision;
        this.radix = radix;
        this.zero = new Apfloat(0, precision, radix);
        this.one = new Apfloat(1, precision, radix);
    }

    public FloatingElement newElement() {
        return new FloatingElement(this);
    }

    public BigInteger getOrder() {
        return BigInteger.ZERO;
    }

    public FloatingElement getNqr() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public int getLengthInBytes() {
        return -1;
    }

    public int getPrecision() {
        return precision;
    }

    public int getRadix() {
        return radix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FloatingField that = (FloatingField) o;

        if (precision != that.precision) return false;
        if (radix != that.radix) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = precision;
        result = 31 * result + radix;
        return result;
    }

}
