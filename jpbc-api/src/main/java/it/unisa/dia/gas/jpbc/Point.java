package it.unisa.dia.gas.jpbc;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public interface Point<E extends Element> extends Element {

    E getX();

    E getY();
}
