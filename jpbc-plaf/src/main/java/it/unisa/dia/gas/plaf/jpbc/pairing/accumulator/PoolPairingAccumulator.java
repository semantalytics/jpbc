package it.unisa.dia.gas.plaf.jpbc.pairing.accumulator;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingPreProcessing;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.Pool;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.PoolExecutor;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.accumultor.Accumulator;

import java.util.concurrent.Callable;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class PoolPairingAccumulator extends PoolExecutor<Element> implements PairingAccumulator {

    private Element value;
    private Pairing pairing;


    public PoolPairingAccumulator(Pairing pairing) {
        this(pairing, pairing.getGT().newOneElement());
    }

    public PoolPairingAccumulator(Pairing pairing, Element value) {
        this.pairing = pairing;
        this.value = value;
    }

    public Accumulator<Element> accumulate(Callable<Element> callable) {
        throw new IllegalStateException("Cannot invoke this method");
    }

    public Element getResult() {
        return value;
    }

    @Override
    public PoolPairingAccumulator process() {
        super.process();

        return this;
    }

    @Override
    public Pool submit(Callable<Element> callable) {
        throw new IllegalStateException("Cannot invoke this method");
    }

    @Override
    public Pool submit(Runnable runnable) {
        throw new IllegalStateException("Cannot invoke this method");
    }

    public PairingAccumulator addPairing(final Element e1, final Element e2) {
        super.submit(new Callable<Element>() {
            public Element call() throws Exception {
                return pairing.pairing(e1, e2);
            }
        });

        return this;
    }

    public PairingAccumulator addPairingInverse(final Element e1, final Element e2) {
        super.submit(new Callable<Element>() {
            public Element call() throws Exception {
                return pairing.pairing(e1, e2).invert();
            }
        });

        return this;
    }

    public PairingAccumulator addPairing(final PairingPreProcessing pairingPreProcessing, final Element e2) {
        super.submit(new Callable<Element>() {
            public Element call() throws Exception {
                return pairingPreProcessing.pairing(e2);
            }
        });

        return this;
    }

    public Element doFinal() {
        process();

        return value;
    }

    @Override
    public void reduce(Element value) {
        this.value.mul(value);
    }
}
