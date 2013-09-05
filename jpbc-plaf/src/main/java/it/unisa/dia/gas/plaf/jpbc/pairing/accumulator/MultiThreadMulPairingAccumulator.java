package it.unisa.dia.gas.plaf.jpbc.pairing.accumulator;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class MultiThreadMulPairingAccumulator extends AbstractPairingAccumulator {


    public MultiThreadMulPairingAccumulator(Pairing pairing) {
        super(pairing);
    }

    public MultiThreadMulPairingAccumulator(Pairing pairing, Element value) {
        super(pairing, value);
    }


    protected void reduce(Element value) {
        this.result.mul(value);
    }
}
