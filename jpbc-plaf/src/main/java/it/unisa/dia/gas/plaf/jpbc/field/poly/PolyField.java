package it.unisa.dia.gas.plaf.jpbc.field.poly;

import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.field.generic.GenericFieldOver;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PolyField<F extends Field> extends GenericFieldOver<F, PolyElement> {


    public PolyField(Field targetField) {
        super((F) targetField);
    }
    

    public PolyElement newElement() {
        return new PolyElement(this);
    }

    public BigInteger getOrder() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public PolyElement getNqr() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public int getFixedLengthInBytes() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

}
