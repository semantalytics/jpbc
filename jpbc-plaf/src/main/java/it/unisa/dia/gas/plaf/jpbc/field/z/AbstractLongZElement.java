package it.unisa.dia.gas.plaf.jpbc.field.z;

import it.unisa.dia.gas.plaf.jpbc.field.base.AbstractElement;
import it.unisa.dia.gas.plaf.jpbc.field.base.AbstractField;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public abstract class AbstractLongZElement<F extends AbstractField> extends AbstractElement<F> {

    public long value;

    protected AbstractLongZElement(F field) {
        super(field);
    }
}
