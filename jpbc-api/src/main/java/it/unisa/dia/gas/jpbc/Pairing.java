package it.unisa.dia.gas.jpbc;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public interface Pairing {

    boolean isSymmetric();

    BigInteger getPhikonr();

    
    Field getG1();

    Field getG2();

    Field getGT();

    Field getZr();

    
    Element pairing(Element g1, Element g2);

    Element finalPow(Element element);

}
