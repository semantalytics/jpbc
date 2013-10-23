package it.unisa.dia.gas.plaf.jpbc.pairing.immutable;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Field;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 2.0.0
 */
public class ImmutableField implements Field {

    Field field;

    public ImmutableField(Field field) {
        this.field = field;
    }

    public Element newElement() {
        return field.newElement().getImmutable();
    }

    public Element newElement(int value) {
        return field.newElement(value).getImmutable();
    }

    public Element newElement(BigInteger value) {
        return field.newElement(value).getImmutable();
    }

    public Element newZeroElement() {
        return field.newZeroElement().getImmutable();
    }

    public Element newOneElement() {
        return field.newOneElement().getImmutable();
    }

    public Element newElementFromHash(byte[] source, int offset, int length) {
        return field.newElementFromHash(source, offset, length);
    }

    public Element newElementFromBytes(byte[] source) {
        return field.newElementFromBytes(source);
    }

    public Element newElementFromBytes(byte[] source, int offset) {
        return field.newElementFromBytes(source, offset);
    }

    public Element newRandomElement() {
        return field.newRandomElement().getImmutable();
    }

    public BigInteger getOrder() {
        return field.getOrder();
    }

    public boolean isOrderOdd() {
        return field.isOrderOdd();
    }

    public Element getNqr() {
        return field.getNqr();
    }

    public int getLengthInBytes() {
        return field.getLengthInBytes();
    }

    public int getCanonicalRepresentationLengthInBytes() {
        return field.getCanonicalRepresentationLengthInBytes();
    }

    public Element[] twice(Element[] elements) {
        // TODO: finish the following methods..
        return field.twice(elements);
    }

    public Element[] add(Element[] a, Element[] b) {
        return field.add(a, b);
    }

    public ElementPowPreProcessing getElementPowPreProcessingFromBytes(byte[] source) {
        return field.getElementPowPreProcessingFromBytes(source);
    }

    public ElementPowPreProcessing getElementPowPreProcessingFromBytes(byte[] source, int offset) {
        return field.getElementPowPreProcessingFromBytes(source, offset);
    }
}
