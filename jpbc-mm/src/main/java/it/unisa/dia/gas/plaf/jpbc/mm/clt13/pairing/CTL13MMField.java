package it.unisa.dia.gas.plaf.jpbc.mm.clt13.pairing;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters.AbstractCTL13MMInstance;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class CTL13MMField implements Field<CTL13MMElement> {

    private SecureRandom random;
    private AbstractCTL13MMInstance instance;
    private int index;


    public CTL13MMField(SecureRandom random, AbstractCTL13MMInstance instance) {
        this(random, instance, 0);
    }

    public CTL13MMField(SecureRandom random, AbstractCTL13MMInstance instance, int index) {
        this.random = random;
        this.instance = instance;
        this.index = index;
    }


    public CTL13MMElement newElement() {
        return (CTL13MMElement) new CTL13MMElement(this, index).setToOne();
    }

    public CTL13MMElement newElement(int value) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public CTL13MMElement newElement(BigInteger value) {
        return new CTL13MMElement(this, instance.encodeAt(value, index), index);
    }

    public CTL13MMElement newZeroElement() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public CTL13MMElement newOneElement() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public CTL13MMElement newRandomElement() {
        return newElement(instance.sampleAtZero());
    }

    public BigInteger getOrder() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public boolean isOrderOdd() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public CTL13MMElement getNqr() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public int getLengthInBytes() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public Element[] twice(Element[] elements) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public Element[] add(Element[] a, Element[] b) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public ElementPowPreProcessing pow(byte[] source) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public ElementPowPreProcessing pow(byte[] source, int offset) {
        throw new IllegalStateException("Not Implemented yet!");
    }


    public AbstractCTL13MMInstance getInstance() {
        return instance;
    }

}
