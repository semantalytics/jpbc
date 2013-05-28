package it.unisa.dia.gas.plaf.jpbc.pairing.accumulator;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class PairingAccumulatorFactory {

    private static final PairingAccumulatorFactory INSTANCE = new PairingAccumulatorFactory();

    public static PairingAccumulatorFactory getInstance() {
        return INSTANCE;
    }

    private boolean multiThreadSupport;


    private PairingAccumulatorFactory() {
        this.multiThreadSupport = Runtime.getRuntime().availableProcessors() > 1;
    }


    public PairingAccumulator getPairingMultiplier(Pairing pairing) {
        return isMultiThreadSupport() ? new MultiThreadPairingAccumulator(pairing)
                : new DefaultPairingAccumulator(pairing);
    }

    public PairingAccumulator getPairingMultiplier(Pairing pairing, Element element) {
        return isMultiThreadSupport() ? new MultiThreadPairingAccumulator(pairing, element)
                : new DefaultPairingAccumulator(pairing, element);
    }

    public boolean isMultiThreadSupport() {
        return multiThreadSupport;
    }

    public void setMultiThreadSupport(boolean multiThreadSupport) {
        this.multiThreadSupport = multiThreadSupport;
    }
}
