package it.unisa.dia.gas.plaf.jpbc.field.vector;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Vector;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class DiagonalMatrixElement<E extends Element> extends AbstractMatrixElement<E, MatrixField> {

    protected final Vector base;
    protected final int baseLength;

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

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[\n");
        for (int i = 0; i < field.n; i++) {

            for (int j = 0; j < field.m; j++) {
                if (isZeroAt(i, j))
                    sb.append(String.format("%10s", "0"));
                else
                    sb.append(String.format("%10s", getAt(i, j)));
                if (j != field.m -1)
                    sb.append(",");
            }

            if (i != field.n -1)
                sb.append(";\n");
        }
        sb.append("]\n");

        return "DiagonalMatrixElement{" +
                "matrix=\n" + sb.toString() +
                '}';
    }


}
