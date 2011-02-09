package it.unisa.dia.gas.plaf.jpbc.field.generic;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.FieldOver;
import it.unisa.dia.gas.jpbc.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public abstract class GenericVectorElement<E extends Element> extends GenericElement implements Vector<E> {

    protected FieldOver field;
    protected List<E> coeff;


    protected GenericVectorElement(FieldOver field) {
        super(field);

        this.field = field;
        this.coeff = new ArrayList<E>();
    }


    public E[] toArray() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public E getAt(int index) {
        return coeff.get(index);
    }

    public int getLength() {
        return coeff.size();
    }

}