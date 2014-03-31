package it.unisa.dia.gas.plaf.jpbc.field.vector;

import it.unisa.dia.gas.jpbc.*;
import it.unisa.dia.gas.plaf.jpbc.field.base.AbstractMatrixElement;
import it.unisa.dia.gas.plaf.jpbc.sampler.Sampler;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class MatrixElement<E extends Element> extends AbstractMatrixElement<E, MatrixField> implements Matrix<E> {


    public MatrixElement(MatrixField field) {
        super(field);

        this.matrix = new Element[field.n][field.m];
        for (int i = 0; i < field.n; i++) {
            for (int j = 0; j < field.m; j++) {
                matrix[i][j] = field.getTargetField().newElement();
            }
        }
    }

    public MatrixElement(MatrixElement e) {
        super(e.getField());

        this.matrix = new Element[field.n][field.m];
        for (int i = 0; i < field.n; i++) {
            for (int j = 0; j < field.m; j++) {
                matrix[i][j] = e.getAt(i, j).duplicate();
            }
        }
    }

    public MatrixElement(MatrixField field, Sampler<BigInteger> sampler) {
        super(field);

        this.matrix = new Element[field.n][field.m];
        for (int i = 0; i < field.n; i++) {
            for (int j = 0; j < field.m; j++) {
                matrix[i][j] = field.getTargetField().newElement(sampler.sample());
            }
        }
    }


    public boolean isSqr() {
        return false;
    }

    public MatrixField getField() {
        return super.getField();
    }

    public MatrixElement<E> duplicate() {
        return new MatrixElement<E>(this);
    }

    public Element getImmutable() {
//        return new ImmutableVectorElement<E>(this);
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public MatrixElement<E> set(Element e) {
//        MatrixElement<E> element = (MatrixElement<E>) e;
//
//        for (int i = 0; i < coefficients.size(); i++) {
//            coefficients.get(i).set(element.coefficients.get(i));
//        }
//
//        return this;
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public MatrixElement<E> set(int value) {
//        coefficients.get(0).set(value);
//
//        for (int i = 1; i < field.n; i++) {
//            coefficients.get(i).setToZero();
//        }
//
//        return this;
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public MatrixElement<E> set(BigInteger value) {
//        coefficients.get(0).set(value);
//
//        for (int i = 1; i < field.n; i++) {
//            coefficients.get(i).setToZero();
//        }
//
//        return this;
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public MatrixElement<E> setToRandom() {
        for (int i = 0; i < field.n; i++)
            for (int j = 0; j < field.m; j++)
                matrix[i][j].setToRandom();

        return this;
    }

    public MatrixElement<E> setFromHash(byte[] source, int offset, int length) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public MatrixElement<E> setToZero() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public boolean isZero() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public MatrixElement<E> setToOne() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public boolean isOne() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public MatrixElement<E> map(Element e) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public MatrixElement<E> twice() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public MatrixElement<E> square() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public MatrixElement<E> invert() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public MatrixElement<E> negate() {
        for (int i = 0; i < field.n; i++)
            for (int j = 0; j < field.m; j++)
                matrix[i][j].negate();

        return this;
    }

    public MatrixElement<E> add(Element e) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public MatrixElement<E> sub(Element e) {
        MatrixElement m = (MatrixElement) e;

        for (int i = 0; i < field.n; i++) {
            for (int j = 0; j < field.m; j++) {
                matrix[i][j].sub(m.getAt(i, j));
            }
        }

        return this;
    }

    public Element mul(Element e) {
        if (field.getTargetField().equals(e.getField())) {
            Element result = e.duplicate();
            if (field.n == 1) {
                for (int j = 0; j < field.m; j++) {
                    result.add(matrix[0][j]);
                }
            } else if (field.m == 1) {
                for (int i = 0; i < field.n; i++) {
                    result.add(matrix[i][0]);
                }
            } else {
                for (int i = 0; i < field.n; i++) {
                    for (int j = 0; j < field.m; j++) {
                        matrix[i][j].mul(e);
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
                            result.add(matrix[0][j]);
                        }
                    } else if (field.m == 1) {
                        for (int i = 0; i < field.n; i++) {
                            result.add(matrix[i][0]);
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

    public MatrixElement<E> mul(int z) {
        for (int i = 0; i < field.n; i++) {
            for (int j = 0; j < field.m; j++) {
                matrix[i][j].mul(z);
            }
        }

        return this;
    }

    public MatrixElement<E> mul(BigInteger n) {
//        for (int i = 0; i < field.n; i++) {
//            coefficients.get(i).mul(n);
//        }
//
//        return this;
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public int sign() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public boolean isEqual(Element e) {
        MatrixElement<E> element = (MatrixElement<E>) e;

        if (field.n != element.getField().n)
            return false;
        if (field.m != element.getField().m)
            return false;

        for (int i = 0; i < field.n; i++)
            for (int j = 0; j < field.m; j++)
                if (!matrix[i][j].isEqual(element.matrix[i][j]))
                    return false;

        return true;
    }

    public int setFromBytes(byte[] source) {
        return setFromBytes(source, 0);
    }

    public int setFromBytes(byte[] source, int offset) {
//        int len = offset;
//        for (int i = 0, size = coefficients.size(); i < size; i++) {
//            len+=coefficients.get(i).setFromBytes(source, len);
//        }
//        return len-offset;
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public byte[] toBytes() {
//        byte[] buffer = new byte[field.getLengthInBytes()];
//        int targetLB = field.getTargetField().getLengthInBytes();
//
//        for (int len = 0, i = 0, size = coefficients.size(); i < size; i++, len += targetLB) {
//            byte[] temp = coefficients.get(i).toBytes();
//            System.arraycopy(temp, 0, buffer, len, targetLB);
//        }
//        return buffer;
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public BigInteger toBigInteger() {
        return matrix[0][0].toBigInteger();
    }

    public boolean equals(Object obj) {
        if (obj instanceof MatrixElement)
            return isEqual((Element) obj);
        return super.equals(obj);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[\n");
        for (int i = 0; i < field.n; i++) {

            for (int j = 0; j < field.m; j++) {
                sb.append(String.format("%10s", matrix[i][j]));
                if (j != field.m -1)
                    sb.append(",");
            }
            if (i != field.n -1)
                sb.append(";\n");
        }
        sb.append("]\n");

        return "MatrixElement{" +
                "matrix=\n" + sb.toString() +
                '}';
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

    public Vector<E> rowAt(int row) {
        VectorField<Field> f = new VectorField<Field>(field.getRandom(), field.getTargetField(), field.m);
        VectorElement r = f.newElement();

        for (int i = 0; i < f.n; i++) {
            r.getAt(i).set(matrix[row][i]);
        }

        return r;
    }

    public Vector<E> columnAt(int col) {
        VectorField<Field> f = new VectorField<Field>(field.getRandom(), field.getTargetField(), field.n);
        VectorElement r = f.newElement();

        for (int i = 0; i < f.n; i++) {
            r.getAt(i).set(matrix[i][col]);
        }

        return r;
    }

    public MatrixElement<E> setRowAt(int row, Element rowElement) {
        Vector r = (Vector) rowElement;

        for (int i = 0; i < field.m; i++) {
            matrix[row][i].set(r.getAt(i));
        }

        return this;
    }

    public MatrixElement<E> setColAt(int col, Element colElement) {
        Vector r = (Vector) colElement;

        for (int i = 0; i < field.n; i++) {
            matrix[i][col].set(r.getAt(i));
        }

        return this;
    }

    public Matrix<E> setSubMatrixToIdentityAt(int row, int col, int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j)
                    matrix[row + i][col + j].setToOne();
                else
                    matrix[row + i][col + j].setToZero();
            }
        }

        return this;
    }

    public Matrix<E> setSubMatrixFromMatrixAt(int row, int col, Element e) {
        // TODO: check the lengths

        Matrix m = (Matrix) e;
        for (int i = 0; i < m.getN(); i++) {
            for (int j = 0; j < m.getM(); j++) {
                matrix[row + i][col + j].set(m.getAt(i, j));
            }
        }

        return this;
    }

    public Matrix<E> setSubMatrixFromMatrixTransposeAt(int row, int col, Element e) {
        // TODO: check the lengths

        Matrix m = (Matrix) e;
        for (int i = 0; i < m.getM(); i++) {
            for (int j = 0; j < m.getN(); j++) {
                matrix[row + i][col + j].set(m.getAt(j, i));
            }
        }

        return this;
    }

    public Matrix<E> mulByTranspose() {
        MatrixField resultField = new MatrixField<Field>(field.getRandom(), field.getTargetField(), field.n);
        MatrixElement result = resultField.newElement();

        for (int i = 0; i < field.n; i++) {

            for (int j = 0; j < field.n; j++) {
                Element temp = field.getTargetField().newZeroElement();

                for (int k = 0; k < field.m; k++) {
                    temp.add(getAt(i, k).duplicate().mul(getAt(j, k)));
                }

                result.getAt(i, j).set(temp);
            }
        }

        return result;
    }

    public Matrix<E> transform(Transformer transformer) {
        for (int i = 0; i < field.n; i++)
            for (int j = 0; j < field.m; j++)
                transformer.transform(i, j, matrix[i][j]);

        return this;
    }

    public boolean isSymmetric() {
        int n = matrix.length;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (!matrix[i][j].equals(matrix[j][i]))
                    return false;
            }
        }

        return true;
    }

    public boolean isSquare() {
        return field.n ==  field.m;
    }



}