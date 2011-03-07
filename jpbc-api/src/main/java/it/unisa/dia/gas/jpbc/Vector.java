package it.unisa.dia.gas.jpbc;


/**
 * TODO: add javadoc
 *
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.2.0
 */
public interface Vector<E extends Element> extends Element {

    /**
     * @since 1.2.0
     */
    int getLength();

    /**
     * @since 1.2.0
     */
    E getAt(int index);

}
