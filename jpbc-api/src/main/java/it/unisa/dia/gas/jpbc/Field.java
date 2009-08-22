package it.unisa.dia.gas.jpbc;

import java.math.BigInteger;

/**
 * Represents an algebraic structure.
 *
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public interface Field<E extends Element> {

    /**
     * Returns a new element which lies in this field.
     *
     * @return a new element which lies in this field.
     */
    E newElement();

    /**
     * Returns a new element whose value is zero.
     *
     * @return a new element whose value is zero.
     */
    E newZeroElement();

    /**
     * Returns a new element whose value is one.
     *
     * @return a new element whose value is one.
     */
    E newOneElement();

    /**
     * Returns a new random element.
     *
     * @return a new random element.
     */
    E newRandomElement();
    
    /**
     * Returns the order of this field.
     *
     * @return the order of this field. Returns 0 for infinite order.
     */

    BigInteger getOrder();

    /**
     * Returns a quadratic non-residue in this field. It returns always the same element.
     *
     * @return a quadratic non-residue in this field.
     */
    E getNqr();

    /**
     * Returns the length in bytes needed to represent an element of this Field.
     *
     * @return the length in bytes needed to represent an element of this Field.
     */
    int getLengthInBytes();

}
