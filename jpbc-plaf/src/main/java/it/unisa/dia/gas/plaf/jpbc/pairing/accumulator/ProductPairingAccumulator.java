package it.unisa.dia.gas.plaf.jpbc.pairing.accumulator;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingPreProcessing;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.Pool;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.accumultor.Accumulator;

import java.util.concurrent.Callable;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class ProductPairingAccumulator implements PairingAccumulator {

    private Pairing pairing;

    private int cursor;
    private Element[] in1, in2;
    private Element result;


    public ProductPairingAccumulator(Pairing pairing, int n) {
        this.pairing = pairing;
        this.in1 = new Element[n];
        this.in2 = new Element[n];
        this.cursor = 0;
    }

    public Accumulator<Element> accumulate(Callable<Element> callable) {
        throw new IllegalStateException("Not supported!!!");
    }

    public Accumulator<Element> process() {
        doFinal();

        return this;
    }

    public Element getResult() {
        return result;
    }

    public Pool submit(Callable<Element> callable) {
        throw new IllegalStateException("Not supported!!!");
    }

    public Pool submit(Runnable runnable) {
        throw new IllegalStateException("Not supported!!!");
    }



    public PairingAccumulator addPairing(Element e1, Element e2) {
        in1[cursor] =  e1;
        in2[cursor++] =  e2;

        return this;
    }

    public PairingAccumulator addPairing(PairingPreProcessing pairingPreProcessing, Element e2) {
        throw new IllegalStateException("Not supported!!!");
    }

    public PairingAccumulator addPairingInverse(Element e1, Element e2) {
        throw new IllegalStateException("Not supported!!!");
    }

    public Element doFinal(){
        return (result = pairing.pairing(in1, in2));
    }

}
