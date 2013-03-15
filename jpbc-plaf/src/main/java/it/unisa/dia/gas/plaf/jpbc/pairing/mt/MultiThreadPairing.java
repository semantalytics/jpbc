package it.unisa.dia.gas.plaf.jpbc.pairing.mt;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingPreProcessing;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.0.0
 */
public class MultiThreadPairing implements Pairing {

    private Pairing pairing;


    public MultiThreadPairing(Pairing pairing) {
        this.pairing = pairing;
    }


    public boolean isSymmetric() {
        return pairing.isSymmetric();
    }

    public Field getG1() {
        return pairing.getG1();
    }

    public Field getG2() {
        return pairing.getG2();
    }

    public Field getGT() {
        return pairing.getGT();
    }

    public Field getZr() {
        return pairing.getZr();
    }

    public Element pairing(Element in1, Element in2) {
        return pairing.pairing(in1, in2);
    }

    public Element pairing(Element[] in1, Element[] in2) {
        return pairing.pairing(in1, in2);
    }

    public PairingPreProcessing pairing(Element in1) {
        return pairing.pairing(in1);
    }

    public int getPairingPreProcessingLengthInBytes() {
        return pairing.getPairingPreProcessingLengthInBytes();
    }

    public PairingPreProcessing pairing(byte[] source) {
        return pairing.pairing(source);
    }

    public PairingPreProcessing pairing(byte[] source, int offset) {
        return pairing.pairing(source, offset);
    }

    public boolean isAlmostCoddh(Element a, Element b, Element c, Element d) {
        return pairing.isAlmostCoddh(a, b, c, d);
    }

    public PairingFieldIdentifier getPairingFieldIdentifier(Field field) {
        return pairing.getPairingFieldIdentifier(field);
    }

}
