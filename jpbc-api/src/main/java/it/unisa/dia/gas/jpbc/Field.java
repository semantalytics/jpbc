package it.unisa.dia.gas.jpbc;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public interface Field<E extends Element> {

    E newElement();

    E newZeroElement();

    E newOneElement();

    BigInteger getOrder();

    E getNqr();

    int getFixedLengthInBytes();

}
