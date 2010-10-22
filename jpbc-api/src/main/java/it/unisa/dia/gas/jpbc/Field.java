package it.unisa.dia.gas.jpbc;

import java.math.BigInteger;

/**
 * Represents an algebraic structure.
 *
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.0.0
 */
public interface Field<E extends Element> {

    /**
     * Returns a new element which lies in this field.
     *
     * @return a new element which lies in this field.
     * @since 1.0.0
     */
    E newElement();

    /**
     * Returns a new element whose value is passed as parameter.
     *
     * @param value the value of the new element.
     * @return a new element whose value is passed as parameter.
     * @see Element#set(int)
     * @since 1.0.0
     */
    E newElement(int value);

    /**
     * Returns a new element whose value is passed as parameter.
     * 
     * @param value the value of the new element.
     * @return a new element whose value is passed as parameter.
     * @see Element#set(java.math.BigInteger)
     * @since 1.0.0
     */
    E newElement(BigInteger value);

    /**
     * Returns a new element whose value is zero.
     *
     * @return a new element whose value is zero.
     * @since 1.0.0
     */
    E newZeroElement();

    /**
     * Returns a new element whose value is one.
     *
     * @return a new element whose value is one.
     * @since 1.0.0
     */
    E newOneElement();

    /**
     * Returns a new random element.
     *
     * @return a new random element.
     * @since 1.0.0
     */
    E newRandomElement();

    /**
     * Returns the order of this field.
     *
     * @return the order of this field. Returns 0 for infinite order.
     * @since 1.0.0
     */
    BigInteger getOrder();

    /**
     * Returns a quadratic non-residue in this field. It returns always the same element.
     *
     * @return a quadratic non-residue in this field.
     * @since 1.0.0
     */
    E getNqr();

    /**
     * Returns the length in bytes needed to represent an element of this Field.
     *
     * @return the length in bytes needed to represent an element of this Field.
     * @since 1.0.0
     */
    int getLengthInBytes();

    /**
     *
     * @param elements
     * @return
     * @sinse 1.1.0
     */
    E[] twice(E[] elements);

    /**
     *
     * @return
     * @sinse 1.1.0
     */
    E[] add(E[] e1, E[] e2);

}
