package it.unisa.dia.gas.jpbc;

import java.util.List;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.0.0
 * TODO: write javadocs
 */
public interface Polynomial<E extends Element> extends Element {

    int getDegree();

    List<E> getCoefficients();

    E getCoefficient(int index);

}
