package it.unisa.dia.gas.plaf.jpbc.field.quadratic;

import it.unisa.dia.gas.jpbc.Field;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class QuadraticTwoField<F extends Field> extends QuadraticEvenField<F, QuadraticTwoElement> {

    public QuadraticTwoField(F targetField) {
        super(targetField);
    }


    public QuadraticTwoElement newElement() {
        return new QuadraticTwoElement(this);
    }

}
