package it.unisa.dia.gas.plaf.jpbc.pairing.combiner;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingPreProcessing;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.2.2
 */
public class DefaultPairingMultiplier implements PairingCombiner {

    private Pairing pairing;
    private Element value;


    public DefaultPairingMultiplier(Pairing pairing) {
        this.pairing = pairing;
        this.value = pairing.getGT().newOneElement();
    }

    public DefaultPairingMultiplier(Pairing pairing, Element value) {
        this.pairing = pairing;
        this.value = value;
    }


    public PairingCombiner addPairing(Element e1, Element e2) {
        value.mul(pairing.pairing(e1, e2));

        return this;
    }

    public PairingCombiner addPairing(PairingPreProcessing pairingPreProcessing, Element e2) {
        value.mul(pairingPreProcessing.pairing(e2));

        return this;
    }

    public PairingCombiner addPairingInverse(Element e1, Element e2) {
        value.mul(pairing.pairing(e1, e2).invert());

        return this;
    }

    public Element doFinal(){
        return value;
    }

}
