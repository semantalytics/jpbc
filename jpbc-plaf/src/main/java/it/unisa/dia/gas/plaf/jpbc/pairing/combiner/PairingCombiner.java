package it.unisa.dia.gas.plaf.jpbc.pairing.combiner;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.PairingPreProcessing;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.2.1
 */
public interface PairingCombiner {

    public PairingCombiner addPairing(Element e1, Element e2);

    public PairingCombiner addPairingInverse(Element e1, Element e2);

    public PairingCombiner addPairing(PairingPreProcessing pairingPreProcessing, Element e2);

    public Element doFinal();

}
