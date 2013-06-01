package it.unisa.dia.gas.plaf.jpbc.pairing.accumulator;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.PairingPreProcessing;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.accumultor.Accumulator;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public interface PairingAccumulator extends Accumulator<Element> {

    public PairingAccumulator addPairing(Element e1, Element e2);

    public PairingAccumulator addPairingInverse(Element e1, Element e2);

    public PairingAccumulator addPairing(PairingPreProcessing pairingPreProcessing, Element e2);

}
