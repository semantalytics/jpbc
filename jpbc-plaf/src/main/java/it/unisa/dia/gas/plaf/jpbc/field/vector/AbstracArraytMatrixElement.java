package it.unisa.dia.gas.plaf.jpbc.field.vector;

import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public abstract class AbstracArraytMatrixElement<E extends Element, F extends AbstractMatrixField> extends AbstractMatrixElement<E, F> {

    protected Element[][] matrix;

    protected AbstracArraytMatrixElement(F field) {
        super(field);
    }

    public E getAt(int row, int col) {
        return (E) matrix[row][col];
    }

}