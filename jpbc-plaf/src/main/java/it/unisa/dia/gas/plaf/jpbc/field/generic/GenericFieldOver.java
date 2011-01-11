package it.unisa.dia.gas.plaf.jpbc.field.generic;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.FieldOver;

import java.util.Random;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public abstract class GenericFieldOver<F extends Field, E extends Element> extends GenericField<E> implements FieldOver<F, E> {
    protected F targetField;

    protected GenericFieldOver(Random random, F targetField) {
        super(random);
        this.targetField = targetField;
    }

//    public GenericFieldOver(F targetField) {
//        this(new SecureRandom(), targetField);
//    }


    public F getTargetField() {
        return targetField;
    }

}
