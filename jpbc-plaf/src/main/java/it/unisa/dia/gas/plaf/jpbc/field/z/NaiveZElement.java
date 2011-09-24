package it.unisa.dia.gas.plaf.jpbc.field.z;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.util.Arrays;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class NaiveZElement extends NaiveAbstractElement {

//    protected BigInteger value;


    public NaiveZElement(Field field) {
        super(field);

        this.value = BigInteger.ZERO;
    }

    public NaiveZElement(Field field, BigInteger value) {
        super(field);

        this.value = value;
    }

    public NaiveZElement(NaiveZElement naiveZrElement) {
        super(naiveZrElement.getField());

        this.value = naiveZrElement.value;
    }


    @Override
    public Element getImmutable() {
        if (isImmutable())
            return this;

        throw new IllegalStateException("Not implemented yet!!!");
    }

    public NaiveZElement duplicate() {
        return new NaiveZElement(this);
    }

    public NaiveZElement set(Element value) {
        this.value = ((NaiveAbstractElement) value).value;

        return this;
    }

    public NaiveZElement set(int value) {
        this.value = BigInteger.valueOf(value);

        return this;
    }

    public NaiveZElement set(BigInteger value) {
        this.value = value;

        return this;
    }

    public boolean isZero() {
        return BigInteger.ZERO.equals(value);
    }

    public boolean isOne() {
        return BigInteger.ONE.equals(value);
    }

    public NaiveZElement twice() {
//        this.value = value.multiply(BigIntegerUtils.TWO);
        this.value = value.add(value);

        return this;
    }

    public NaiveZElement mul(int z) {
        this.value = this.value.multiply(BigInteger.valueOf(z));

        return this;
    }

    public NaiveZElement setToZero() {
        this.value = BigInteger.ZERO;

        return this;
    }

    public NaiveZElement setToOne() {
        this.value = BigInteger.ONE;

        return this;
    }

    public NaiveZElement setToRandom() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public NaiveZElement setFromHash(byte[] source, int offset, int length) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public int setFromBytes(byte[] source) {
        return setFromBytes(source, 0);
    }

    public int setFromBytes(byte[] source, int offset) {
        byte[] buffer = Arrays.copyOf(source, offset, field.getLengthInBytes());
        value = new BigInteger(1, buffer);

        return buffer.length;
    }

    public NaiveZElement square() {
//        value = value.modPow(BigIntegerUtils.TWO, order);
        value = value.multiply(value);

        return this;
    }

    public NaiveZElement invert() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public NaiveZElement halve() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public NaiveZElement negate() {
        value = value.multiply(BigInteger.valueOf(-1));

        return this;
    }

    public NaiveZElement add(Element element) {
        value = value.add(((NaiveAbstractElement)element).value);

        return this;
    }

    public NaiveZElement sub(Element element) {
        value = value.subtract(((NaiveAbstractElement)element).value);

        return this;
    }

    public NaiveZElement div(Element element) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public NaiveZElement mul(Element element) {
        value = value.multiply(((NaiveAbstractElement)element).value);

        return this;
    }

    public NaiveZElement mul(BigInteger n) {
        this.value = this.value.multiply(n);

        return this;
    }

    public NaiveZElement mulZn(Element z) {
        this.value = this.value.multiply(z.toBigInteger());

        return this;
    }

    public boolean isSqr() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public NaiveZElement sqrt() {
        throw new IllegalStateException("Not implemented yet!!!");
    }


    public NaiveZElement pow(BigInteger n) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public NaiveZElement powZn(Element n) {
        return pow(n.toBigInteger());
    }

    public boolean isEqual(Element e) {
        return this == e || value.compareTo(((NaiveZElement) e).value) == 0;

    }

    public BigInteger toBigInteger() {
        return value;
    }

    @Override
    public byte[] toBytes() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public int sign() {
        return value.signum();
    }

    public String toString() {
        return value.toString();
    }

    public boolean equals(Object o) {
        if (o instanceof NaiveZElement)
            return isEqual((Element) o);
        return isEqual((NaiveZElement) o);
    }

}
