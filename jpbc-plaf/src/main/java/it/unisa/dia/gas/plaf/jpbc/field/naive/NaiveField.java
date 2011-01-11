package it.unisa.dia.gas.plaf.jpbc.field.naive;

import it.unisa.dia.gas.plaf.jpbc.field.generic.GenericField;
import it.unisa.dia.gas.plaf.jpbc.util.BigIntegerUtils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class NaiveField extends GenericField<NaiveElement> {
    protected BigInteger order;
    public NaiveElement nqr;
    protected int fixedLengthInBytes;


    public NaiveField(BigInteger order) {
        this(new SecureRandom(), order, null);
    }

    public NaiveField(Random random, BigInteger order) {
        this(random, order, null);
    }

    public NaiveField(BigInteger order, BigInteger nqr) {
        this(new SecureRandom(), order, nqr);
    }

    public NaiveField(Random random, BigInteger order, BigInteger nqr) {
        super(random);
        this.order = order;
        this.orderIsOdd = BigIntegerUtils.isOdd(order);

        this.fixedLengthInBytes = (order.bitLength() + 7) / 8;

        if (nqr != null)
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
