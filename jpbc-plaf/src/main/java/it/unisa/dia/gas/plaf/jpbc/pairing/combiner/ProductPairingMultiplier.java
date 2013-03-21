package it.unisa.dia.gas.plaf.jpbc.pairing.combiner;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingPreProcessing;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.2.2
 */
public class ProductPairingMultiplier implements PairingCombiner {

    private Pairing pairing;

    private int index;
    private Element[] in1, in2;


    public ProductPairingMultiplier(Pairing pairing, int n) {
        this.pairing = pairing;
        this.in1 = new Element[n];
        this.in2 = new Element[n];
        this.index = 0;
    }


    public PairingCombiner addPairing(Element e1, Element e2) {
        in1[index] =  e1;
        in2[index++] =  e1;

        return this;
    }

    public PairingCombiner addPairing(PairingPreProcessing pairingPreProcessing, Element e2) {
        throw new IllegalStateException("Not supported!!!");
    }

    public PairingCombiner addPairingInverse(Element e1, Element e2) {
        throw new IllegalStateException("Not supported!!!");
    }

    public Element combine(){
        return pairing.pairing(in1, in2);
    }

}
