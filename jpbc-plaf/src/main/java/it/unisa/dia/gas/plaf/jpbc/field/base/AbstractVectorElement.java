package it.unisa.dia.gas.plaf.jpbc.field.base;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.FieldOver;
import it.unisa.dia.gas.jpbc.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public abstract class AbstractVectorElement<E extends Element> extends AbstractElement implements Vector<E> {

    protected FieldOver field;
    protected List<E> coeff;


    protected AbstractVectorElement(FieldOver field) {
        super(field);

        this.field = field;
        this.coeff = new ArrayList<E>();
    }


    public E getAt(int index) {
        return coeff.get(index);
    }

    public E innerProduct(Vector<E> v) {
        Element sum = field.newZeroElement();
        for (int i = 0; i < coeff.size(); i++) {
            sum.add(coeff.get(i).duplicate().add(v.getAt(i)));
        }
        return (E) sum;
    }

    public int getSize() {
        return coeff.size();
    }

}