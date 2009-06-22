package it.unisa.dia.gas.plaf.jpbc.field.generic;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public abstract class GenericField<E extends Element> implements Field<E> {

    public E newZeroElement() {
        E e = newElement();
        e.setToZero();

        return e;
    }

    public E newOneElement() {
        E e = newElement();
        e.setToOne();

        return e;
    }

    public E sub(E e1, E e2) {
        return (E) newElement().set(e1).sub(e2);
    }
}
