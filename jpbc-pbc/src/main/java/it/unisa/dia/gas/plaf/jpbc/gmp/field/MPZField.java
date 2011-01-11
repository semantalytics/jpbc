package it.unisa.dia.gas.plaf.jpbc.gmp.field;

import it.unisa.dia.gas.plaf.jpbc.field.generic.GenericField;
import it.unisa.dia.gas.plaf.jpbc.util.BigIntegerUtils;
import it.unisa.dia.gas.plaf.jpbc.wrapper.jna.MPZElementType;

import java.math.BigInteger;
import java.util.Random;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class MPZField extends GenericField<MPZElement> {

    protected BigInteger bigIntOrder;
    public MPZElementType order;

    public MPZElement nqr;
    protected int fixedLengthInBytes;


    public MPZField(Random random, BigInteger order) {
        super(random);

        this.bigIntOrder = order;
        this.order = MPZElementType.fromBigInteger(order);
        this.orderIsOdd = BigIntegerUtils.isOdd(order);
        
        this.fixedLengthInBytes = (order.bitLength() + 7) / 8;
    }

    public MPZField(Random random, BigInteger order, BigInteger nqr) {
        this(random, order);
        if (nqr != null)
            this.nqr = newElement().set(nqr);
    }

    
    public MPZElement newElement() {
        return new MPZElement(this);
    }

    public BigInteger getOrder() {
        return bigIntOrder;
    }

    public MPZElement getNqr() {
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
