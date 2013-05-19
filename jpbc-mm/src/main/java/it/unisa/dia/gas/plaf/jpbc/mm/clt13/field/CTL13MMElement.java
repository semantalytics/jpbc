package it.unisa.dia.gas.plaf.jpbc.mm.clt13.field;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Field;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class CTL13MMElement implements Element {

    protected CTL13MMField field;

    protected BigInteger value;
    protected int index;


    public CTL13MMElement(CTL13MMField field, BigInteger value, int index) {
        this.field = field;
        this.value = value;
        this.index = index;
    }


    public CTL13MMElement(CTL13MMField field, int index) {
        this.field = field;
        this.index = index;
    }


    public Field getField() {
        return field;
    }

    public int getLengthInBytes() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public boolean isImmutable() {
        return false;
    }

    public Element getImmutable() {
        throw new IllegalStateException("Not Implemented yet!");
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
        this.value = field.getInstance().sampleAtLevel(index);

        return this;
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
        this.value = field.getInstance().encodeZeroAt(index);

        return this;
    }

    public boolean isZero() {
        return field.getInstance().isZero(value, index);
    }

    public Element setToOne() {
        this.value = field.getInstance().encodeOneAt(index);

        return this;
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
        throw new IllegalStateException("Not Implemented yet!");
    }

    public Element add(Element element) {
        if (index != ((CTL13MMElement)element).index)
            throw new IllegalStateException("Index not valid!");

        this.value = field.getInstance().reduce(this.value.add(((CTL13MMElement) element).value));

        return this;
    }

    public Element sub(Element element) {
        if (index != ((CTL13MMElement)element).index)
            throw new IllegalStateException("Index not valid!");

        this.value = field.getInstance().reduce(this.value.subtract(((CTL13MMElement) element).value));

        return this;
    }

    public Element mul(Element element) {
        this.index += ((CTL13MMElement) element).index;
        this.value = field.getInstance().reduce(value.multiply(((CTL13MMElement) element).value));
        this.field = field.getInstance().getFieldAt(index);

        return this;
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
        if (((CTL13MMElement) n).index != 0)
            throw new IllegalArgumentException("Level must be zero!");

        this.value = field.getInstance().encodeAt(((CTL13MMElement) n).value, index);
        if (index == 1)
            this.value = field.getInstance().reRandomize(value, index);

        return this;
    }

    public ElementPowPreProcessing pow() {
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
