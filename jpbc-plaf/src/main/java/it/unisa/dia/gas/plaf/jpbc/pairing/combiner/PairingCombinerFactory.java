package it.unisa.dia.gas.plaf.jpbc.pairing.combiner;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.combiner.mt.MultiThreadPairingMultiplier;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class PairingCombinerFactory {

    private static final PairingCombinerFactory INSTANCE = new PairingCombinerFactory();

    public static PairingCombinerFactory getInstance() {
        return INSTANCE;
    }

    private boolean multiThreadSupport;


    private PairingCombinerFactory() {
        this.multiThreadSupport = Runtime.getRuntime().availableProcessors() > 1;
    }


    public PairingCombiner getPairingMultiplier(Pairing pairing) {
        return isMultiThreadSupport() ? new MultiThreadPairingMultiplier(pairing)
                : new SequentialPairingMultiplier(pairing);
    }

    public PairingCombiner getPairingMultiplier(Pairing pairing, Element element) {
        return isMultiThreadSupport() ? new MultiThreadPairingMultiplier(pairing, element)
                : new SequentialPairingMultiplier(pairing, element);
    }

    public boolean isMultiThreadSupport() {
        return multiThreadSupport;
    }

    public void setMultiThreadSupport(boolean multiThreadSupport) {
        this.multiThreadSupport = multiThreadSupport;
    }
}
