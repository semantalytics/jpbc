package it.unisa.dia.gas.plaf.jpbc.field.z;

import it.unisa.dia.gas.plaf.jpbc.field.base.AbstractField;
import it.unisa.dia.gas.plaf.jpbc.util.math.BigIntegerUtils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class NaiveSymmetricZrField extends AbstractField<NaiveSymmetricZrElement> {
    protected BigInteger order;
    protected NaiveSymmetricZrElement nqr;
    protected int fixedLengthInBytes;
    protected BigInteger twoInverse;


    public NaiveSymmetricZrField(BigInteger order) {
        this(new SecureRandom(), order, null);
    }

    public NaiveSymmetricZrField(Random random, BigInteger order) {
        this(random, order, null);
    }

    public NaiveSymmetricZrField(BigInteger order, BigInteger nqr) {
        this(new SecureRandom(), order, nqr);
    }

    public NaiveSymmetricZrField(Random random, BigInteger order, BigInteger nqr) {
        super(random);
        this.order = order;
        this.orderIsOdd = BigIntegerUtils.isOdd(order);

        this.fixedLengthInBytes = (order.bitLength() + 7) / 8;

        this.twoInverse = BigIntegerUtils.TWO.modInverse(order);

        if (nqr != null)
            this.nqr = newElement().set(nqr);
    }


    public NaiveSymmetricZrElement newElement() {
        return new NaiveSymmetricZrElement(this);
    }

    public BigInteger getOrder() {
        return order;
    }

    public NaiveSymmetricZrElement getNqr() {
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
