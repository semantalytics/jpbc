package it.unisa.dia.gas.jpbc;

import java.util.List;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public interface Polynomial<E extends Element> extends Element {

    List<E> getCoeff();

    E getCoeffAt(int index);

    int getDegree();

}
