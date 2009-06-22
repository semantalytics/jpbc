package it.unisa.dia.gas.jpbc;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public interface Pairing {

    boolean isSymmetric();

    Field getG1();

    Field getG2();

    Field getGT();

    Field getZr();

    BigInteger getPhikonr();
    
    
    Element pairing(Element g1, Element g2);

    Element finalPow(Element element);

}
