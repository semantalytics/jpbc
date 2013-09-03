package it.unisa.dia.gas.plaf.jpbc.mm.clt13.pairing;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class CTL13MMImmutableElement extends CTL13MMElement {


    public CTL13MMImmutableElement(CTL13MMField field, BigInteger value, int index) {
        super(field, value, index);
    }


    public CTL13MMImmutableElement(CTL13MMField field, int index) {
        super(field, index);
    }


    public boolean isImmutable() {
        return true;
    }

    public Element getImmutable() {
        return this;
    }

    public Element duplicate() {
        return new CTL13MMElement(field, value, index);
    }

    public Element set(Element value) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public Element set(int value) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public Element set(BigInteger value) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public BigInteger toBigInteger() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public Element setToRandom() {
        return duplicate().setToRandom();
    }

    public Element setFromHash(byte[] source, int offset, int length) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public int setFromBytes(byte[] source) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public int setFromBytes(byte[] source, int offset) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public byte[] toBytes() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public Element setToZero() {
        return duplicate().setToZero();
    }

    public boolean isZero() {
        return field.getInstance().isZero(value, index);
    }

    public Element setToOne() {
        return duplicate().setToOne();
    }

    public boolean isEqual(Element value) {
        return duplicate().sub(value).isZero();
    }

    public boolean isOne() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public Element twice() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public Element square() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public Element invert() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public Element halve() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public Element negate() {
        return duplicate().negate();
    }

    public Element add(Element element) {
        return duplicate().add(element);
    }

    public Element sub(Element element) {
        return duplicate().sub(element);
    }

    public Element mul(Element element) {
        return duplicate().mul(element);
    }

    public Element mul(int z) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public Element mul(BigInteger n) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public Element mulZn(Element z) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public Element div(Element element) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public Element pow(BigInteger n) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public Element powZn(Element n) {
        return duplicate().powZn(n);
    }

    public ElementPowPreProcessing getElementPowPreProcessing() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public Element sqrt() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public boolean isSqr() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public int sign() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    @Override
    public String toString() {
        return String.format("{%s,%d}", value.toString(), index);
    }
}
