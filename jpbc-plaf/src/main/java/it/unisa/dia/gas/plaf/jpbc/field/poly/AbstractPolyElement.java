package it.unisa.dia.gas.plaf.jpbc.field.poly;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Polynomial;
import it.unisa.dia.gas.jpbc.Vector;
import it.unisa.dia.gas.plaf.jpbc.field.base.AbstractElement;
import it.unisa.dia.gas.plaf.jpbc.field.base.AbstractFieldOver;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public abstract class AbstractPolyElement<E extends Element, F extends AbstractFieldOver>
        extends AbstractElement<F> implements Polynomial<E> {

    protected List<E> coefficients;


    protected AbstractPolyElement(F field) {
        super(field);

        this.coefficients = new ArrayList<E>();
    }


    public int getSize() {
        return coefficients.size();
    }

    public E getAt(int index) {
        return coefficients.get(index);
    }

    public List<E> getCoefficients() {
        return coefficients;
    }

    public E getCoefficient(int index) {
        return coefficients.get(index);
    }

    public int getDegree() {
        return coefficients.size();
    }

    public Vector add(Element e, int offset) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public Vector<E> subVectorTo(int end) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public Vector<E> subVectorFrom(int start) {
        throw new IllegalStateException("Not Implemented yet!");
    }

}