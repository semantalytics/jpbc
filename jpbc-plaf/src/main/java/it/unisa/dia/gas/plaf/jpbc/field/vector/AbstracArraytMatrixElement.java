package it.unisa.dia.gas.plaf.jpbc.field.vector;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Matrix;

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

    public final Matrix<E> setAt(int row, int col, E e) {
        matrix[row][col].set(e);

        return this;
    }

    @Override
    public Matrix<E> setZeroAt(int row, int col) {
        matrix[row][col].setToZero();

        return this;
    }

    @Override
    public Matrix<E> setOneAt(int row, int col) {
        matrix[row][col].setToOne();

        return this;
    }
}