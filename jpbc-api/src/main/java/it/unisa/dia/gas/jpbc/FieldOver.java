package it.unisa.dia.gas.jpbc;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public interface FieldOver<F extends Field, E extends Element> extends Field<E> {

    F getTargetField();

}
