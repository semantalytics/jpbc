package it.unisa.dia.gas.plaf.jpbc.field.generic;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.FieldOver;
import it.unisa.dia.gas.jpbc.Polynomial;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public abstract class GenericPolyElement<E extends Element> extends GenericElement implements Polynomial<E> {

    protected FieldOver field;
    protected List<E> coeff;


    protected GenericPolyElement(FieldOver field) {
        super(field);

        this.field = field;
        this.coeff = new ArrayList<E>();
    }


    public List<E> getCoefficients() {
        return coeff;
    }

    public E getCoefficient(int index) {
        return coeff.get(index);
    }

    public int getDegree() {
        return coeff.size();
    }
}