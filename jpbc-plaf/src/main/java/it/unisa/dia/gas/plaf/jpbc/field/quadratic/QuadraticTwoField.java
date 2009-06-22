package it.unisa.dia.gas.plaf.jpbc.field.quadratic;

import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.field.generic.GenericFieldOver;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class QuadraticTwoField<F extends Field> extends GenericFieldOver<F, QuadraticTwoElement> {
    protected BigInteger order;
    protected int fixedLengthInBytes;

    public QuadraticTwoField(F targetField) {
        super(targetField);

        this.order = targetField.getOrder().multiply(targetField.getOrder());

        if (targetField.getFixedLengthInBytes() < 0) {
            //f->length_in_bytes = fq_length_in_bytes;
            fixedLengthInBytes = -1;
        } else {
            fixedLengthInBytes = 2 * targetField.getFixedLengthInBytes();
        }
    }


    public QuadraticTwoElement newElement() {
        return new QuadraticTwoElement(this);
    }

    public BigInteger getOrder() {
        return order;
    }

    public QuadraticTwoElement getNqr() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public int getFixedLengthInBytes() {
        return fixedLengthInBytes;
    }
}
