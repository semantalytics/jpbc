package it.unisa.dia.gas.jpbc;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.0.0
 */
public interface FieldOver<F extends Field, E extends Element> extends Field<E> {

    F getTargetField();

}
