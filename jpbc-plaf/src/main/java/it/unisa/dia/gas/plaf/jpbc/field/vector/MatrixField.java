package it.unisa.dia.gas.plaf.jpbc.field.vector;


import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Matrix;
import it.unisa.dia.gas.plaf.jpbc.field.base.AbstractFieldOver;
import it.unisa.dia.gas.plaf.jpbc.sampler.Sampler;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class MatrixField<F extends Field> extends AbstractMatrixField<F, MatrixElement> {


    public static Matrix newElementFromSampler(MatrixField field, int n, int m, Sampler<BigInteger> sampler) {
        return new MatrixField<Field>(field.getRandom(), field.getTargetField(), n, m).newElementFromSampler(sampler);
    }


    protected int lenInBytes;

    public MatrixField(SecureRandom random, F targetField, int n, int m) {
        super(random, targetField, n, m);

        this.lenInBytes = n * m * targetField.getLengthInBytes();
    }

    public MatrixField(SecureRandom random, F targetField, int n) {
        super(random, targetField, n, n);

        this.lenInBytes = n * n * targetField.getLengthInBytes();
    }

    public MatrixElement newElement() {
        return new MatrixElement(this);
    }

    public BigInteger getOrder() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public int getLengthInBytes() {
        return lenInBytes;
    }

    public MatrixElement getNqr() {
        throw new IllegalStateException("Not implemented yet!!!");
    }


    public int getN() {
        return n;
    }

    public int getM() {
        return m;
    }

    public MatrixElement newElementFromSampler(Sampler<BigInteger> sampler) {
        return new MatrixElement(this, sampler);
    }


    public MatrixField<F> getNewMatrixField(int newN, int newM) {
        return new MatrixField<F>(random, targetField, newN, newM);
    }

    public MatrixField<F> getNewMatrixField(int newN) {
        return new MatrixField<F>(random, targetField, newN);
    }



    public Element newIdentity() {
        MatrixElement m = new MatrixElement(this);

        for (int i = 0; i < n; i++) {
            m.getAt(i, i).setToOne();
        }
        return m;
    }


    public Matrix union(Element a, Element b) {
        MatrixElement a1 = (MatrixElement) a;
        MatrixElement b1 = (MatrixElement) b;

        MatrixField f = new MatrixField(random, targetField, a1.getField().n,
                a1.getField().m + b1.getField().m);

        MatrixElement c = f.newElement();
        for (int i = 0; i < f.n; i++) {
            for (int j = 0; j < a1.getField().m; j++) {
                c.getAt(i, j).set(a1.getAt(i, j));
            }

            for (int j = 0; j < b1.getField().m; j++) {
                c.getAt(i, a1.getField().m + j).set(b1.getAt(i, j));
            }
        }


        return c;

    }

    public Element unionByRow(Element a, Element b) {
        MatrixElement a1 = (MatrixElement) a;
        MatrixElement b1 = (MatrixElement) b;

        MatrixField f = new MatrixField(
                random, targetField, a1.getField().n + b1.getField().n,
                a1.getField().m
        );

        MatrixElement c = f.newElement();
        for (int i = 0; i < f.m; i++) {

            for (int j = 0; j < a1.getField().n; j++) {
                c.getAt(j, i).set(a1.getAt(j,i));
            }

            for (int j = 0; j < b1.getField().n; j++) {
                c.getAt(a1.getField().n + j,i).set(b1.getAt(j,i));
            }
        }


        return c;

    }


    public MatrixElement newDiagonalElement(Element g) {
        MatrixElement r = newElement();

        if (g instanceof VectorElement) {
            VectorElement vg = (VectorElement) g;

            int col = 0;
            for (int row = 0; row < n; row++) {

                for (int k = 0; k < vg.getSize(); k++) {
                    r.getAt(row, col).set(vg.getAt(k));
                    col += 1;
                }
            }

        }

        return r;
    }

}
