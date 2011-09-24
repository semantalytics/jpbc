package it.unisa.dia.gas.plaf.jpbc.field.z;

import it.unisa.dia.gas.plaf.jpbc.field.generic.AbstractField;
import it.unisa.dia.gas.plaf.jpbc.util.math.BigIntegerUtils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class NaiveZrField extends AbstractField<NaiveZrElement> {
    protected BigInteger order;
    protected NaiveZrElement nqr;
    protected int fixedLengthInBytes;
    protected BigInteger twoInverse;


    public NaiveZrField(BigInteger order) {
        this(new SecureRandom(), order, null);
    }

    public NaiveZrField(Random random, BigInteger order) {
        this(random, order, null);
    }

    public NaiveZrField(BigInteger order, BigInteger nqr) {
        this(new SecureRandom(), order, nqr);
    }

    public NaiveZrField(Random random, BigInteger order, BigInteger nqr) {
        super(random);
        this.order = order;
        this.orderIsOdd = BigIntegerUtils.isOdd(order);

        this.fixedLengthInBytes = (order.bitLength() + 7) / 8;

        this.twoInverse = BigIntegerUtils.TWO.modInverse(order);

        if (nqr != null)
            this.nqr = newElement().set(nqr);
    }


    public NaiveZrElement newElement() {
        return new NaiveZrElement(this);
    }

    public BigInteger getOrder() {
        return order;
    }

    public NaiveZrElement getNqr() {
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
