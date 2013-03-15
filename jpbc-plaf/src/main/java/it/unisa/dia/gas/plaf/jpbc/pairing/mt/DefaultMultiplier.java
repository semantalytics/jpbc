package it.unisa.dia.gas.plaf.jpbc.pairing.mt;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.0.0
 */
public class DefaultMultiplier implements Multiplier {

    private Pairing pairing;
    private Element value;

    public DefaultMultiplier(Pairing pairing, Field field) {
        this.pairing = pairing;
        this.value = field.newOneElement();
    }

    public DefaultMultiplier(Pairing pairing, Element value) {
        this.pairing = pairing;
        this.value = value;
    }


    public Multiplier addPairing(Element e1, Element e2) {
        value.mul(pairing.pairing(e1, e2));

        return this;
    }

    public Element finish(){
        return value;
    }

}
