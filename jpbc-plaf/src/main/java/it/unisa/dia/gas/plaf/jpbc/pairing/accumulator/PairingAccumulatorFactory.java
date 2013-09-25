package it.unisa.dia.gas.plaf.jpbc.pairing.accumulator;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class PairingAccumulatorFactory {

    private static final PairingAccumulatorFactory INSTANCE = new PairingAccumulatorFactory();

    public static PairingAccumulatorFactory getInstance() {
        return INSTANCE;
    }

    private boolean multiThreadSupport;


    private PairingAccumulatorFactory() {
        this.multiThreadSupport = false; // Runtime.getRuntime().availableProcessors() > 1;
    }


    public PairingAccumulator getPairingMultiplier(Pairing pairing) {
        return isMultiThreadSupport() ? new MultiThreadMulPairingAccumulator(pairing)
                : new SequentialMulPairingAccumulator(pairing);
    }

    public PairingAccumulator getPairingMultiplier(Pairing pairing, Element element) {
        return isMultiThreadSupport() ? new MultiThreadMulPairingAccumulator(pairing, element)
                : new SequentialMulPairingAccumulator(pairing, element);
    }

    public boolean isMultiThreadSupport() {
        return multiThreadSupport;
    }

    public void setMultiThreadSupport(boolean multiThreadSupport) {
        this.multiThreadSupport = multiThreadSupport;
    }
}
