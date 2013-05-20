package it.unisa.dia.gas.plaf.jpbc.mm.clt13.pairing;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingPreProcessing;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters.CTL13MMInstance;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class CTL13MMPairing implements Pairing {

    private CTL13MMInstance instance;


    public CTL13MMPairing(CTL13MMInstance instance) {
        this.instance = instance;
    }


    public boolean isSymmetric() {
        return true;
    }

    public Field getG1() {
        return instance.getFieldAt(1);
    }

    public Field getG2() {
        return instance.getFieldAt(1);
    }

    public Field getGT() {
        return instance.getFieldAt(2);
    }

    public Field getZr() {
        return instance.getFieldAt(0);
    }

    public int getDegree() {
        return instance.getParameters().getKappa();
    }

    public Field getFieldAt(int index) {
        return instance.getFieldAt(index);
    }

    public Element pairing(Element in1, Element in2) {
        CTL13MMElement a = (CTL13MMElement) in1;
        CTL13MMElement b = (CTL13MMElement) in2;

        int index = a.index + b.index;
        BigInteger value = instance.reduce(a.value.multiply(b.value));

        return new CTL13MMElement(instance.getFieldAt(index), value, index);
    }

    public boolean isProductPairingSupported() {
        return false;  
    }

    public Element pairing(Element[] in1, Element[] in2) {
        return null;  
    }

    public PairingPreProcessing pairing(Element in1) {
        return null;  
    }

    public int getPairingPreProcessingLengthInBytes() {
        return 0;  
    }

    public PairingPreProcessing pairing(byte[] source) {
        return null;  
    }

    public PairingPreProcessing pairing(byte[] source, int offset) {
        return null;  
    }

    public boolean isAlmostCoddh(Element a, Element b, Element c, Element d) {
        return false;  
    }

    public PairingFieldIdentifier getPairingFieldIdentifier(Field field) {
        return null;  
    }

    public Field getField(PairingFieldIdentifier id) {
        return null;  
    }
}
