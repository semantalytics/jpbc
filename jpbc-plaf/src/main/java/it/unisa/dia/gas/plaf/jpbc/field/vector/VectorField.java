package it.unisa.dia.gas.plaf.jpbc.field.vector;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.field.base.AbstractField;
import it.unisa.dia.gas.plaf.jpbc.field.base.AbstractFieldOver;
import it.unisa.dia.gas.plaf.jpbc.sampler.Sampler;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class VectorField<F extends Field> extends AbstractFieldOver<F, VectorElement> {


    public static Element newRandomElement(Field targetField, int n) {
        return new VectorField<Field>(((AbstractField)targetField).getRandom(),targetField, n).newRandomElement();
    }



    protected final int n, lenInBytes;

    public VectorField(SecureRandom random, F targetField, int n) {
        super(random, targetField);

        this.n = n;
        this.lenInBytes = n * targetField.getLengthInBytes();
    }


    public VectorElement newElement() {
        return new VectorElement(this);
    }

    public VectorElement newElementFromSampler(Sampler<BigInteger> sampler) {
        return new VectorElement(this, sampler);
    }

    public VectorElement newPrimitiveElement() {
        VectorElement g = newElement();

        BigInteger value = BigInteger.ONE;
        for (int i = 0; i < n; i++) {
            g.getAt(i).set(value);
            value = value.shiftLeft(1);
        }

        return g;
    }

    public BigInteger getOrder() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public VectorElement getNqr() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public int getLengthInBytes() {
        return lenInBytes;
    }

    public int getN() {
        return n;
    }

}
