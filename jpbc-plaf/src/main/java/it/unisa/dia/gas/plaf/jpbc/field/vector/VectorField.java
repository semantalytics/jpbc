package it.unisa.dia.gas.plaf.jpbc.field.vector;

import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.field.generic.GenericFieldOver;

import java.math.BigInteger;
import java.util.Random;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class VectorField<F extends Field> extends GenericFieldOver<F, VectorElement> {
    protected int n;


    public VectorField(Random random, F targetField, int n) {
        super(random, targetField);
        this.n = n;
    }


    public VectorElement newElement() {
        return new VectorElement(this);
    }

    public BigInteger getOrder() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public VectorElement getNqr() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public int getLengthInBytes() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public int getN() {
        return n;
    }

}
