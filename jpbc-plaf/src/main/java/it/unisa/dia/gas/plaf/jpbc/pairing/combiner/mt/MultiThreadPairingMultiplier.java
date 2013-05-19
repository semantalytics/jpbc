package it.unisa.dia.gas.plaf.jpbc.pairing.combiner.mt;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingPreProcessing;
import it.unisa.dia.gas.plaf.jpbc.pairing.combiner.PairingCombiner;
import it.unisa.dia.gas.plaf.jpbc.util.mt.MultiThreadExecutor;

import java.util.concurrent.Callable;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class MultiThreadPairingMultiplier extends MultiThreadExecutor<Element> implements PairingCombiner {

    private Element value;
    private Pairing pairing;


    public MultiThreadPairingMultiplier(Pairing pairing) {
        this(pairing, pairing.getGT().newOneElement());
    }

    public MultiThreadPairingMultiplier(Pairing pairing, Element value) {
        this.pairing = pairing;
        this.value = value;
    }

    public PairingCombiner addPairing(final Element e1, final Element e2) {
        submit(new Callable<Element>() {
            public Element call() throws Exception {
                return pairing.pairing(e1, e2);
            }
        });

        return this;
    }

    public PairingCombiner addPairingInverse(final Element e1, final Element e2) {
        submit(new Callable<Element>() {
            public Element call() throws Exception {
                return pairing.pairing(e1, e2).invert();
            }
        });

        return this;
    }

    public PairingCombiner addPairing(final PairingPreProcessing pairingPreProcessing, final Element e2) {
        submit(new Callable<Element>() {
            public Element call() throws Exception {
                return pairingPreProcessing.pairing(e2);
            }
        });

        return this;
    }

    public Element combine() {
        process();

        return value;
    }

    @Override
    public void reduce(Element value) {
        this.value.mul(value);
    }
}
