package it.unisa.dia.gas.plaf.jpbc.field.z;

import it.unisa.dia.gas.plaf.jpbc.field.base.AbstractField;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class NaiveZField extends AbstractField<NaiveZElement> {

    public NaiveZField() {
        this(new SecureRandom());
    }

    public NaiveZField(Random random) {
        super(random);
    }


    public NaiveZElement newElement() {
        return new NaiveZElement(this);
    }

    public BigInteger getOrder() {
        return BigInteger.ZERO;
    }

    public NaiveZElement getNqr() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public int getLengthInBytes() {
        return -1;
    }
    
}
