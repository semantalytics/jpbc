package it.unisa.dia.gas.plaf.jpbc.field.quadratic;

import it.unisa.dia.gas.jpbc.Field;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class DegreeTwoQuadraticField<F extends Field> extends QuadraticField<F, DegreeTwoQuadraticElement> {

    public DegreeTwoQuadraticField(F targetField) {
        super(targetField);
    }


    public DegreeTwoQuadraticElement newElement() {
        return new DegreeTwoQuadraticElement(this);
    }

}
