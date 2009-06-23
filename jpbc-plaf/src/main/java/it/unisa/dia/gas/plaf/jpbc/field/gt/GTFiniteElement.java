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


    public GTFiniteElement duplicate() {
        return new GTFiniteElement(pairing, (GTFiniteField) field, value);
    }

    public GTFiniteElement set(Element element) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public GTFiniteElement set(int value) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public GTFiniteElement set(BigInteger value) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public boolean isZero() {
        return isOne();
    }

    public boolean isOne() {
        return value.isOne();
    }

    public GTFiniteField getField() {
        return (GTFiniteField) field;
    }

    public GTFiniteElement setToZero() {
        value.setToOne();
        
        return this;
    }

    public GTFiniteElement setToOne() {
        value.setToOne();

        return this;
    }

    public GTFiniteElement setToRandom() {
        value.setToRandom();
        pairing.finalPow(value);

        return this;
    }

    public GTFiniteElement setFromHash(byte[] hash) {
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

    public GTFiniteElement invert() {
        value.invert();
        
        return this;
    }

    public GTFiniteElement negate() {
        return invert();
    }

    public GTFiniteElement add(Element element) {
        return mul(element);
    }

    public GTFiniteElement sub(Element element) {
        return div(element);
    }

    public GTFiniteElement div(Element element) {
        value.div(((GTFiniteElement) element).value);

        return this;
    }

    public GTFiniteElement mul(Element element) {
        value.mul(((GTFiniteElement) element).value);

        return this;
    }

    public GTFiniteElement mul(BigInteger value) {
        return pow(value);
    }

    public boolean isSqr() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public GTFiniteElement sqrt() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public GTFiniteElement pow(BigInteger value) {
        this.value.pow(value);

        return this;
    }

    public int compareTo(Element element) {
        return value.compareTo(((GTFiniteElement) element).value);
    }

    public GTFiniteElement powZn(Element element) {
        this.value.powZn(element);

        return this;
    }

    public BigInteger toBigInteger() {
        return value.toBigInteger();
    }

    @Override
    public byte[] toBytes() {
        return value.toBytes();
    }

    public int sign() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public String toString() {
        return value.toString();
    }

}
