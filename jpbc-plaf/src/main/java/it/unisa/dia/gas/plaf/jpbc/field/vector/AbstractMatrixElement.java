package it.unisa.dia.gas.plaf.jpbc.field.vector;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Matrix;
import it.unisa.dia.gas.plaf.jpbc.field.base.AbstractElement;

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
                    if (!isZeroAt(i, k ))
                        temp.add(getAt(i, k).duplicate().mul(getAt(j, k)));
                }

                result.getAt(i, j).set(temp);
            }
        }

        return result;
    }

}