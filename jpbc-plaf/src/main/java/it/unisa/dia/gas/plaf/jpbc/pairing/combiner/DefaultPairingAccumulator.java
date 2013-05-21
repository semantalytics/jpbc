package it.unisa.dia.gas.plaf.jpbc.pairing.combiner;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingPreProcessing;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class DefaultPairingAccumulator implements PairingAccumulator {

    private Pairing pairing;
    private Element value;


    public DefaultPairingAccumulator(Pairing pairing) {
        this.pairing = pairing;
        this.value = pairing.getGT().newOneElement();
    }

    public DefaultPairingAccumulator(Pairing pairing, Element value) {
        this.pairing = pairing;
        this.value = value;
    }


    public PairingAccumulator addPairing(Element e1, Element e2) {
        value.mul(pairing.pairing(e1, e2));

        return this;
    }

    public PairingAccumulator addPairing(PairingPreProcessing pairingPreProcessing, Element e2) {
        value.mul(pairingPreProcessing.pairing(e2));

        return this;
    }

    public PairingAccumulator addPairingInverse(Element e1, Element e2) {
        value.mul(pairing.pairing(e1, e2).invert());

        return this;
    }

    public Element doFinal(){
        return value;
    }

}
