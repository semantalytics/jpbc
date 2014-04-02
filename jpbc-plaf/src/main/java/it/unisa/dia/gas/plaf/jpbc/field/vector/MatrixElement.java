package it.unisa.dia.gas.plaf.jpbc.field.vector;

import it.unisa.dia.gas.jpbc.*;
import it.unisa.dia.gas.plaf.jpbc.sampler.Sampler;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.ExecutorServiceUtils;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.Pool;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.PoolExecutor;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class MatrixElement<E extends Element> extends AbstracArraytMatrixElement<E, MatrixField> implements Matrix<E> {


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

    public MatrixElement<E> setToRandom() {
        for (int i = 0; i < field.n; i++)
            for (int j = 0; j < field.m; j++)
                matrix[i][j].setToRandom();

        return this;
    }

    public MatrixElement<E> negate() {
        for (int i = 0; i < field.n; i++)
            for (int j = 0; j < field.m; j++)
                matrix[i][j].negate();

        return this;
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

    public Element div(Element e) {
        if (field.getTargetField().equals(e.getField())) {
            for (int i = 0; i < field.n; i++) {
                for (int j = 0; j < field.m; j++) {
                    matrix[i][j].div(e);
                }
            }

            return this;
        } else
            throw new IllegalArgumentException("Not implemented yet!!!");
    }

    public MatrixElement<E> mul(int z) {
        for (int i = 0; i < field.n; i++) {
            for (int j = 0; j < field.m; j++) {
                matrix[i][j].mul(z);
            }
        }

        return this;
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

    public String toStringSubMatrix(int startRow, int startCol) {
        StringBuffer sb = new StringBuffer();
        sb.append("[\n");
        for (int i = startRow; i < field.n; i++) {

            for (int j = startCol; j < field.m; j++) {
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

    public Matrix<E> setSubMatrixToIdentityAt(int row, int col, int n, Element e) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j)
                    matrix[row + i][col + j].set(e);
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

    public Matrix<E> setSubMatrixFromMatrixAt(int row, int col, Element e, Transformer transformer) {
        // TODO: check the lengths

        Matrix m = (Matrix) e;
        for (int i = 0; i < m.getN(); i++) {
            for (int j = 0; j < m.getM(); j++) {
                matrix[row + i][col + j].set(m.getAt(i, j));
                transformer.transform(row + i, col + j, matrix[row + i][col + j]);
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

    public boolean isZeroAt(int row, int col) {
        return matrix[row][col].isZero();
    }


}