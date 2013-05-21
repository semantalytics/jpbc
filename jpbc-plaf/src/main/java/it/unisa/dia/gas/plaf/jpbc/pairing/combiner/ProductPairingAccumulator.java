package it.unisa.dia.gas.plaf.jpbc.pairing.combiner;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingPreProcessing;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class ProductPairingAccumulator implements PairingAccumulator {

    private Pairing pairing;

    private int index;
    private Element[] in1, in2;


    public ProductPairingAccumulator(Pairing pairing, int n) {
        this.pairing = pairing;
        this.in1 = new Element[n];
        this.in2 = new Element[n];
        this.index = 0;
    }


    public PairingAccumulator addPairing(Element e1, Element e2) {
        in1[index] =  e1;
        in2[index++] =  e1;

        return this;
    }

    public PairingAccumulator addPairing(PairingPreProcessing pairingPreProcessing, Element e2) {
        throw new IllegalStateException("Not supported!!!");
    }

    public PairingAccumulator addPairingInverse(Element e1, Element e2) {
        throw new IllegalStateException("Not supported!!!");
    }

    public Element doFinal(){
        return pairing.pairing(in1, in2);
    }

}
