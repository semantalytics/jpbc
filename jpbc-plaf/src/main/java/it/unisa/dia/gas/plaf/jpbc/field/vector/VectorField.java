package it.unisa.dia.gas.plaf.jpbc.field.vector;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.field.base.AbstractFieldOver;
import it.unisa.dia.gas.plaf.jpbc.sampler.Sampler;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class VectorField<F extends Field> extends AbstractFieldOver<F, VectorElement> {
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

    public static VectorElement union(Element a, Element b) {
        VectorElement va = (VectorElement) a;
        VectorElement vb = (VectorElement) b;

        VectorField f = new VectorField(
                va.getField().getRandom(),
                va.getField().getTargetField(),
                va.getSize() + vb.getSize()
        );
        VectorElement r = f.newElement();
        int counter = 0;

        for (int i = 0; i < va.getSize(); i++)
            r.getAt(counter++).set(va.getAt(i));

        for (int i = 0; i < vb.getSize(); i++)
            r.getAt(counter++).set(vb.getAt(i));

        return r;
    }

}
