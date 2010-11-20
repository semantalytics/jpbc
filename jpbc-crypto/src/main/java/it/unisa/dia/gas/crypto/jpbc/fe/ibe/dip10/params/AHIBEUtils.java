package it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.params;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class AHIBEUtils {

    public static Element randomIn(Pairing pairing, Element generator) {
        return generator.powZn(pairing.getZr().newRandomElement());
    }

}
