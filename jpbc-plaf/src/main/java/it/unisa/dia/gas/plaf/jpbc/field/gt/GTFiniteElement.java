package it.unisa.dia.gas.plaf.jpbc.field.gt;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.field.generic.GenericElement;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class GTFiniteElement extends GenericElement {
    protected Pairing pairing;

    public Element value;


    public GTFiniteElement(Pairing pairing, GTFiniteField field) {
        super(field);

        this.pairing = pairing;
        this.value = field.getTargetField().newElement().setToOne();
    }

    public GTFiniteElement(Pairing pairing, GTFiniteField field, Element value) {
        super(field);

        this.pairing = pairing;
        this.value = value.duplicate();
    }


    public Element duplicate() {
        return new GTFiniteElement(pairing, (GTFiniteField) field, value);
    }

    public Element set(Element element) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public Element set(int value) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public Element set(BigInteger value) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public boolean isZero() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public boolean isOne() {
        return value.isOne();
    }

    public Element map(Element e) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public Element twice() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public Element mul(int value) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public GTFiniteField getField() {
        return (GTFiniteField) field;
    }

    public Element setToZero() {
        value.setToOne();
        
        return this;
    }

    public Element setToOne() {
        value.setToOne();

        return this;
    }

    public Element setToRandom() {
        value.setToRandom();
        pairing.finalPow(value);

        return this;
    }

    public Element setFromHash(byte[] hash) {
        value.setFromHash(hash);
        pairing.finalPow(value);

        return this;
    }

    public int setFromBytes(byte[] bytes) {
        return value.setFromBytes(bytes);
    }

    public int setEncoding(byte[] bytes) {
        return value.setEncoding(bytes);
    }

    public byte[] getDecoding() {
        return value.getDecoding();
    }

    public Element square() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public Element invert() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public Element halve() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public Element negate() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public Element add(Element element) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public Element sub(Element element) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public Element div(Element element) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public Element mul(Element element) {
        value.mul(((GTFiniteElement) element).value);

        return this;
    }

    public Element mul(BigInteger value) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public Element mulZn(Element element) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public boolean isSqr() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public Element sqrt() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public Element pow(BigInteger value) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public int compareTo(Element element) {
        return value.compareTo(((GTFiniteElement) element).value);
    }

    public Element powZn(Element element) {
        this.value.powZn(element);

        return this;
    }

    public BigInteger toBigInteger() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public int sign() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public String toString() {
        return value.toString();
    }

}
