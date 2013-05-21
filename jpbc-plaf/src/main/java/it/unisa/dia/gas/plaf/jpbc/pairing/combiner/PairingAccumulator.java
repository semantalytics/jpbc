package it.unisa.dia.gas.plaf.jpbc.pairing.combiner;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.PairingPreProcessing;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.2.1
 */
public interface PairingAccumulator {

    public PairingAccumulator addPairing(Element e1, Element e2);

    public PairingAccumulator addPairingInverse(Element e1, Element e2);

    public PairingAccumulator addPairing(PairingPreProcessing pairingPreProcessing, Element e2);

    public Element doFinal();

}
