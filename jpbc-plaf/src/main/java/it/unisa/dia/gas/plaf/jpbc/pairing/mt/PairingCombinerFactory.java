package it.unisa.dia.gas.plaf.jpbc.pairing.mt;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingCombiner;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.2.1
 */
public class PairingCombinerFactory {

    private static final PairingCombinerFactory INSTANCE = new PairingCombinerFactory();

    public static PairingCombinerFactory getInstance() {
        return INSTANCE;
    }

    private PairingCombinerFactory() {
    }


    public PairingCombiner getPairingMultiplier(Pairing pairing) {
        return new MultiThreadPairingMultiplier(pairing);
    }

    public PairingCombiner getPairingMultiplier(Pairing pairing, Element element) {
        return new MultiThreadPairingMultiplier(pairing, element);
    }

}
