package it.unisa.dia.gas.jpbc;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public interface ElementPowPreProcessing extends PreProcessing {

    Element pow(BigInteger n);

    Element powZn(Element n);

}
