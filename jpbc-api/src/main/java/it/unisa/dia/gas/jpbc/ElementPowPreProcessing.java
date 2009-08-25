package it.unisa.dia.gas.jpbc;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.0.0
 */
public interface ElementPowPreProcessing extends PreProcessing {

    /**
     * todo
     * @param n
     * @return
     * @since 1.0.0
     */
    Element pow(BigInteger n);

    /**
     *
     * @param n
     * @return
     * @since 1.0.0
     */
    Element powZn(Element n);

}
