package it.unisa.dia.gas.jpbc;

/**
 * This element represents a vector through its coordinates.
 * TODO: finish javadocs.
 *
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 1.2.0
 * @see Element
 * @see Polynomial
 * @see Point
 */
public interface Vector<E extends Element> extends Element {

    /**
     * Returns the size of this vector.
     *
     * @return the size of this vector.
     * @since 1.2.0
     */
    int getSize();

    /**
     * Returns the element at the specified coordinate.
     *
     * @param index the index of the requested coordinate.
     * @return the element at the specified coordinate.
     * @since 1.2.0
     */
    E getAt(int index);

    Vector<E> add(Element... es);


    Vector<E> subVectorTo(int end);

    Vector<E> subVectorFrom(int start);
}
