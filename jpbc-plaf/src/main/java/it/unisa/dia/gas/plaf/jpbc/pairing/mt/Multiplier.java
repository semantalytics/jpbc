package it.unisa.dia.gas.plaf.jpbc.pairing.mt;

import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.0.0
 */
public interface Multiplier {

    Multiplier addPairing(Element e1, Element e2);

    Element finish();

}
