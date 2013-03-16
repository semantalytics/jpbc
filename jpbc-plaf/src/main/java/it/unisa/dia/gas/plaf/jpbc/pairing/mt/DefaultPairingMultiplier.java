package it.unisa.dia.gas.plaf.jpbc.pairing.mt;

import it.unisa.dia.gas.jpbc.*;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.0.0
 */
public class DefaultPairingMultiplier implements PairingCombiner {

    private Pairing pairing;
    private Element value;


    public DefaultPairingMultiplier(Pairing pairing, Field field) {
        this.pairing = pairing;
        this.value = field.newOneElement();
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

    public Element doFinal(){
        return value;
    }

}
