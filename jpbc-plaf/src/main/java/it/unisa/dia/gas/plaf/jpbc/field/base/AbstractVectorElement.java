package it.unisa.dia.gas.plaf.jpbc.field.base;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Vector;
import it.unisa.dia.gas.plaf.jpbc.field.vector.VectorField;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public abstract class AbstractVectorElement<E extends Element, F extends AbstractFieldOver> extends AbstractElement<F> implements Vector<E> {

    // TODO: use directly array??
    protected List<E> coeff;

    protected AbstractVectorElement(F field) {
        super(field);

        this.coeff = new ArrayList<E>();
    }


    public E getAt(int index) {
        return coeff.get(index);
    }

    public Vector<E> subVectorFrom(int start) {
        VectorField vf = new VectorField(field.getRandom(), field.getTargetField(), getSize() - start);
        Vector v = vf.newElement();

        for (int i = 0, n = vf.getN(); i < n; i++)
            v.getAt(i).set(getAt(start + i));

        return v;
    }

    public Vector<E> subVectorTo(int end) {
        VectorField vf = new VectorField(field.getRandom(), field.getTargetField(), end);
        Vector v = vf.newElement();

        for (int i = 0; i < end; i++)
            v.getAt(i).set(getAt(i));

        return v;
    }

    public int getSize() {
        return coeff.size();
    }

}