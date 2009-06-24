package it.unisa.dia.gas.plaf.jpbc.field.generic;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.FieldOver;
import it.unisa.dia.gas.jpbc.Point;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public abstract class GenericPointElement extends GenericElement implements Point {

    protected FieldOver field;
    protected Element x, y;


    protected GenericPointElement(FieldOver field) {
        super(field);
        
        this.field = field;
    }


    public Element getX() {
        return x;
    }

    public Element getY() {
        return y;
    }
}