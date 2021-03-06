package it.unisa.dia.gas.plaf.jpbc.field.z;

import it.unisa.dia.gas.plaf.jpbc.field.base.AbstractElement;
import it.unisa.dia.gas.plaf.jpbc.field.base.AbstractField;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public abstract class AbstractBigIntegerZElement<F extends AbstractField> extends AbstractElement<F> {

    public BigInteger value;

    protected AbstractBigIntegerZElement(F field) {
        super(field);
    }

    public BigInteger toBigInteger() {
        return value;
    }

}
