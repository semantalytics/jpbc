package it.unisa.dia.gas.plaf.jpbc.field.naive;

import it.unisa.dia.gas.plaf.jpbc.field.generic.GenericField;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class NaiveField extends GenericField<NaiveElement> {
    protected BigInteger order;
    public NaiveElement nqr;
    protected int fixedLengthInBytes;


    public NaiveField(BigInteger order) {
        this.order = order;
        this.fixedLengthInBytes = (order.bitLength() + 7) / 8;
    }

    public NaiveField(BigInteger order, BigInteger nqr) {
        this(order);
        this.nqr = newElement().set(nqr);
    }

    
    public NaiveElement newElement() {
        return new NaiveElement(this);
    }

    public BigInteger getOrder() {
        return order;
    }

    public NaiveElement getNqr() {
        if (nqr == null) {
            nqr = newElement();
            do {
                nqr.setToRandom();
            } while (nqr.isSqr());
        }
        
        return nqr.duplicate();
    }

    public int getLengthInBytes() {
        return fixedLengthInBytes;
    }
}
