package it.unisa.dia.gas.plaf.jpbc.mm.clt13.pairing;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingPreProcessing;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.engine.CTL13MMInstance;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class CTL13MMPairing implements Pairing {

    protected SecureRandom random;
    protected CTL13MMInstance instance;
    protected CTL13MMField[] fields;


    public CTL13MMPairing(SecureRandom random, CTL13MMInstance instance) {
        this.random = random;
        this.instance = instance;
        this.fields = new CTL13MMField[instance.getParameters().getKappa() + 1];
    }


    public boolean isSymmetric() {
        return true;
    }

    public Field getG1() {
        return getFieldAt(1);
    }

    public Field getG2() {
        return getFieldAt(1);
    }

    public Field getGT() {
        return getFieldAt(2);
    }

    public Field getZr() {
        return getFieldAt(0);
    }

    public int getDegree() {
        return instance.getParameters().getKappa();
    }

    public Field getFieldAt(int index) {
        if (fields[index] == null)
            fields[index] = new CTL13MMField(random, this, index);

        return fields[index];
    }

    public Element pairing(Element in1, Element in2) {
        CTL13MMElement a = (CTL13MMElement) in1;
        CTL13MMElement b = (CTL13MMElement) in2;

        int index = a.index + b.index;
        BigInteger value = instance.reduce(a.value.multiply(b.value));

        return new CTL13MMElement((CTL13MMField) getFieldAt(index), value, index);
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
        return getFieldAt(id.ordinal());
    }

    public CTL13MMInstance getCTL13MMInstance() {
        return instance;
    }
}
