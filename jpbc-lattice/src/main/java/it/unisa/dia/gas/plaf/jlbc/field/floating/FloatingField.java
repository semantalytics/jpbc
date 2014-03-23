package it.unisa.dia.gas.plaf.jlbc.field.floating;

import it.unisa.dia.gas.plaf.jpbc.field.base.AbstractField;
import it.unisa.dia.gas.plaf.jpbc.field.z.ZElement;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class FloatingField extends AbstractField<FloatingElement> {

    protected int precision, radix;


    public FloatingField() {
        this(new SecureRandom());
    }

    public FloatingField(SecureRandom random) {
        super(random);

        this.precision = 128;
        this.radix = 2;
    }

    public FloatingField(SecureRandom random, int precision, int radix) {
        super(random);

        this.precision = precision;
        this.radix = radix;
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
}
