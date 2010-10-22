package it.unisa.dia.gas.plaf.jpbc.field.generic;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public abstract class GenericField<E extends Element> implements Field<E> {

    public E newElement(int value) {
        E e = newElement();
        e.set(value);

        return e;
    }

    public E newElement(BigInteger value) {
        E e = newElement();
        e.set(value);

        return e;
    }

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

    public E newRandomElement() {
        E e = newElement();
        e.setToRandom();

        return e;
    }

    public E[] twice(E[] elements) {
        for (E element : elements) {
            element.twice();
        }
        return elements;
    }

    public E[] add(E[] e1, E[] e2) {
        for (int i = 0; i < e1.length; i++) {
            e1[i].add(e2[i]);
        }

        return e1;
    }

}
