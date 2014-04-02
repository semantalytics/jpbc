package it.unisa.dia.gas.plaf.jpbc.field.vector;

import it.unisa.dia.gas.jpbc.*;
import it.unisa.dia.gas.plaf.jpbc.field.base.AbstractElement;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public abstract class AbstractMatrixElement<E extends Element, F extends AbstractMatrixField> extends AbstractElement<F> implements Matrix<E> {


    protected AbstractMatrixElement(F field) {
        super(field);
    }


    public Matrix<E> mulByTranspose() {
        MatrixField resultField = new MatrixField<Field>(field.getRandom(), field.getTargetField(), field.n);
        Matrix result = resultField.newElement();

        for (int i = 0; i < field.n; i++) {

            for (int j = 0; j < field.n; j++) {
                Element temp = field.getTargetField().newZeroElement();

                for (int k = 0; k < field.m; k++) {
                    if (!isZeroAt(i, k))
                        temp.add(getAt(i, k).duplicate().mul(getAt(j, k)));
                }

                result.getAt(i, j).set(temp);
            }
        }

        return result;
    }

    public Field getTargetField() {
        return field.getTargetField();
    }

    public int getN() {
        return field.n;
    }

    public int getM() {
        return field.m;
    }

    public int sign() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public E getAt(int row, int col) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Vector<E> rowAt(int row) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Vector<E> columnAt(int col) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Matrix<E> setRowAt(int row, Element rowElement) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Matrix<E> setColAt(int col, Element colElement) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Matrix<E> setSubMatrixToIdentityAt(int row, int col, int n) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Matrix<E> setSubMatrixFromMatrixAt(int row, int col, Element e) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Matrix<E> setSubMatrixFromMatrixTransposeAt(int row, int col, Element e) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Matrix<E> transform(Transformer transformer) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public boolean isSymmetric() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public boolean isSquare() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public boolean isZeroAt(int row, int col) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public String toStringSubMatrix(int startRow, int startCol) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Element duplicate() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Element set(Element value) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Element set(int value) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Element set(BigInteger value) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public BigInteger toBigInteger() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Element setToRandom() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Element setFromHash(byte[] source, int offset, int length) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Element setToZero() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public boolean isZero() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Element setToOne() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public boolean isEqual(Element value) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public boolean isOne() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Element invert() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Element negate() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Element add(Element element) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Element mul(BigInteger n) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public boolean isSqr() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Element mul(Element e) {
        if (field.getTargetField().equals(e.getField())) {
            Element result = e.duplicate();
            if (field.n == 1) {
                for (int j = 0; j < field.m; j++) {
                    result.add(getAt(0, j));
                }
            } else if (field.m == 1) {
                for (int i = 0; i < field.n; i++) {
                    result.add(getAt(i, 0));
                }
            } else {
                for (int i = 0; i < field.n; i++) {
                    for (int j = 0; j < field.m; j++) {
                        getAt(i, j).mul(e);
                    }
                }
                return this;
            }

            return result;
        } else if (e instanceof Vector) {
            Vector ve = (Vector) e;

            if (field.getTargetField().equals(((FieldOver) ve.getField()).getTargetField())) {
                if (ve.getSize() == 1) {
                    Element result = ve.getAt(0).duplicate();
                    if (field.n == 1) {
                        for (int j = 0; j < field.m; j++) {
                            result.add(getAt(0, j));
                        }
                    } else if (field.m == 1) {
                        for (int i = 0; i < field.n; i++) {
                            result.add(getAt(i, 0));
                        }
                    } else
                        throw new IllegalArgumentException("Cannot multiply this way.");

                    return result;
                } else {
                    // Check dimensions

                    if (ve.getSize() == field.m) {
                        VectorField f = new VectorField(field.getRandom(), field.getTargetField(), field.n);
                        VectorElement r = f.newElement();

                        for (int i = 0; i < f.n; i++) {

                            // row \times column

                            for (int j = 0; j < 1; j++) {
                                Element temp = field.getTargetField().newElement();
                                for (int k = 0; k < field.m; k++) {
                                    if (getAt(i, k).isZero())
                                        continue;

                                    temp.add(
                                            getAt(i, k).duplicate().mul(ve.getAt(k))
                                    );
                                }
                                r.getAt(i).set(temp);

                            }
                        }

                        return r;
                    } else if (ve.getSize() == field.n) {
                        // Consider transpose

                        VectorField f = new VectorField(field.getRandom(), field.getTargetField(), field.m);
                        VectorElement r = f.newElement();

                        for (int i = 0; i < f.n; i++) {

                            // column \times row
                            Element temp = field.getTargetField().newElement();
                            for (int k = 0; k < field.n; k++) {
                                if (getAt(k, i).isZero())
                                    continue;

                                temp.add(
                                        getAt(k, i).duplicate().mul(ve.getAt(k))
                                );
                            }
                            r.getAt(i).set(temp);
                        }

                        return r;
                    }
                }
            }
            throw new IllegalStateException("Not Implemented yet!!!");
        } else if (e instanceof MatrixElement) {
            MatrixElement me = (MatrixElement) e;

            if (field.getTargetField().equals(me.getField().getTargetField())) {
                MatrixField f = new MatrixField<Field>(field.getRandom(), field.getTargetField(), field.n, me.getField().m);
                MatrixElement r = f.newElement();

                for (int i = 0; i < f.n; i++) {
                    // row \times column
                    for (int j = 0; j < f.m; j++) {
                        Element temp = field.getTargetField().newElement();

                        for (int k = 0; k < field.m; k++) {
                            if (getAt(i, k).isZero())
                                continue;
                            temp.add(getAt(i, k).duplicate().mul(me.getAt(k, j)));
                        }
                        r.getAt(i, j).set(temp);
                    }
                }

                return r;
            }
        }

        throw new IllegalStateException("Not Implemented yet!!!");
    }

}