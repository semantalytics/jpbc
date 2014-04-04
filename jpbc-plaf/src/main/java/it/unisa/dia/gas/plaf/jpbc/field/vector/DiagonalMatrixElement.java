package it.unisa.dia.gas.plaf.jpbc.field.vector;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Vector;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class DiagonalMatrixElement<E extends Element> extends AbstractMatrixElement<E, MatrixField> {

    protected Vector base;
    protected int baseLength;

    public DiagonalMatrixElement(MatrixField field, Vector row) {
        super(field);

        this.base = row;
        this.baseLength = base.getSize();
    }

    @Override
    public boolean isZeroAt(int row, int col) {
        int colOffset = row * base.getSize();
        return !(col >= colOffset && col < colOffset + baseLength);
    }

    @Override
    public E getAt(int row, int col) {
        // TODO: remove this check
        if (isZeroAt(row, col))
            throw new IllegalStateException("Invalid Position");

        return (E) base.getAt(col % base.getSize());
    }
}
