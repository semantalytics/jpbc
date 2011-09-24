package it.unisa.dia.gas.plaf.jpbc.field.z;

import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.field.base.AbstractElement;

import java.math.BigInteger;

/**
 * Created by IntelliJ IDEA.
 * User: angelo
 * Date: 9/24/11
 * Time: 12:47 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class NaiveAbstractElement extends AbstractElement {

    public BigInteger value;

    protected NaiveAbstractElement(Field field) {
        super(field);
    }
}
