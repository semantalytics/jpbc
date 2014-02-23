package it.unisa.dia.gas.plaf.jpbc.field.base;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Matrix;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public abstract class AbstractMatrixElement<E extends Element, F extends AbstractFieldOver> extends AbstractElement<F> implements Matrix<E> {

    protected Element[][] matrix;

    protected AbstractMatrixElement(F field) {
        super(field);
    }


}