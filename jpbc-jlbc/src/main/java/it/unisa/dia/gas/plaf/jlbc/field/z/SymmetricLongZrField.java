package it.unisa.dia.gas.plaf.jlbc.field.z;

import it.unisa.dia.gas.plaf.jpbc.field.base.AbstractField;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class SymmetricLongZrField  extends AbstractField<SymmetricLongZrElement> {

    protected long order;
    protected long halfOrder;
    protected double orderInv;

//    protected SymmetricZrField sfield;


    public SymmetricLongZrField(SecureRandom random, long order) {
        super(random);

        this.order = order;
        this.halfOrder = order / 2;

//        System.out.println("order = " + order);
//        System.out.println("halfOrder = " + halfOrder);
        this.orderInv = 1/ (double) order;
//        this.sfield = new SymmetricZrField(BigInteger.valueOf(order));
    }


    public SymmetricLongZrElement newElement() {
        return new SymmetricLongZrElement(this);
    }

    public BigInteger getOrder() {
        return BigInteger.valueOf(order);
    }

    public SymmetricLongZrElement getNqr() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public int getLengthInBytes() {
        return 4;
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj))
            return true;

        return obj instanceof SymmetricLongZrField && ((SymmetricLongZrField) obj).order == this.order;
    }

}
