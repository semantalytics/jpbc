package it.unisa.dia.gas.plaf.jpbc.field.generic;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.FieldOver;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public abstract class GenericFieldOver<F extends Field, E extends Element> extends GenericField<E> implements FieldOver<F, E> {
    protected F targetField;


    public GenericFieldOver(F targetField) {
        this.targetField = targetField;
    }


    public F getTargetField() {
        return targetField;
    }

}
