package it.unisa.dia.gas.plaf.jpbc.field.naive;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.field.generic.GenericElement;
import it.unisa.dia.gas.plaf.jpbc.util.BigIntegerUtils;
import it.unisa.dia.gas.plaf.jpbc.util.Utils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class NaiveElement extends GenericElement {

    protected BigInteger value;
    protected BigInteger order;
    protected boolean oddOrder;
    protected SecureRandom secureRandom;

    public NaiveElement(Field field) {
        super(field);

        this.value = BigInteger.ZERO;
        this.order = field.getOrder();
        this.oddOrder = BigIntegerUtils.isOdd(order);
    }

    public NaiveElement(Field field, BigInteger value) {
        super(field);

        this.value = value;
        this.order = field.getOrder();
        this.oddOrder = BigIntegerUtils.isOdd(order);
    }

    public NaiveElement(NaiveElement naiveElement) {
        super(naiveElement.getField());

        this.value = naiveElement.value;
        this.order = naiveElement.field.getOrder();
        this.oddOrder = BigIntegerUtils.isOdd(order);
    }


    @Override
    public Element getImmutable() {
        return new ImmutableNaiveElement(this);
    }

    public NaiveElement duplicate() {
        return new NaiveElement(this);
    }

    public NaiveElement set(Element value) {
        if (this.field != value.getField()) {
            // TODO: we should import also the field...
//        this.field = element.field;
        }
        this.value = ((NaiveElement) value).value;

        return this;
    }

    public NaiveElement set(int value) {
        this.value = BigInteger.valueOf(value).mod(order);

        return this;
    }

    public NaiveElement set(BigInteger value) {
        this.value = value.mod(order);

        return this;
    }

    public boolean isZero() {
        return BigInteger.ZERO.equals(value);
    }

    public boolean isOne() {
        return BigInteger.ONE.equals(value);
    }

    public NaiveElement twice() {
//        this.value = value.multiply(BigIntegerUtils.TWO).mod(order);
        this.value = value.add(value).mod(order);

        return this;
    }

    public NaiveElement mul(int z) {
        this.value = this.value.multiply(BigInteger.valueOf(z)).mod(order);

        return this;
    }

    public NaiveElement setToZero() {
        this.value = BigInteger.ZERO;

        return this;
    }

    public NaiveElement setToOne() {
        this.value = BigInteger.ONE;

        return this;
    }

    public NaiveElement setToRandom() {
        if (secureRandom == null)
            secureRandom = new SecureRandom();

        this.value = new BigInteger(order.bitLength(), secureRandom).mod(order);

        return this;
    }

    public NaiveElement setFromHash(byte[] source, int offset, int length) {
        int i = 0, n, count = (order.bitLength() + 7) / 8;

        byte[] buf = new byte[count];
        byte counter = 0;
        boolean done = false;

        for (; ;) {

            if (length >= count - i) {
                n = count - i;
                done = true;
            } else n = length;

            System.arraycopy(source, offset, buf, i, n);
            i += n;

            if (done)
                break;

            buf[i] = counter;
            counter++;
            i++;

            if (i == count) break;
        }
        assert (i == count);

        //mpz_import(z, count, 1, 1, 1, 0, buf);
        BigInteger z = new BigInteger(buf);

        while (z.compareTo(order) > 0) {
            z = z.divide(BigIntegerUtils.TWO);
        }

        this.value = z;

        return this;
    }

    public int setFromBytes(byte[] source) {
        return setFromBytes(source, 0);
    }

    public int setFromBytes(byte[] source, int offset) {
        value = new BigInteger(1, Utils.copyOf(source, offset, field.getLengthInBytes())).mod(order);

        return field.getLengthInBytes();
    }

    public int setEncoding(byte[] bytes) {
        byte[] source = Utils.copyOf(bytes, field.getLengthInBytes() - 1);
        value = new BigInteger(1, source);

        return source.length;
    }

    public byte[] getDecoding() {
        return value.toByteArray();
    }

    public NaiveElement square() {
//        value = value.modPow(BigIntegerUtils.TWO, order);
        value = value.multiply(value).mod(order);

        return this;
    }

    public NaiveElement invert() {
        value = value.modInverse(order);

        return this;
    }

    public NaiveElement halve() {
        value = value.multiply(BigIntegerUtils.TWO.modInverse(order)).mod(order);

        return this;
    }

    public NaiveElement negate() {
        if (isZero()) {
            value = BigInteger.ZERO;
            return this;
        }

        value = order.subtract(value);

        return this;
    }

    public NaiveElement add(Element element) {
        value = value.add(((NaiveElement)element).value).mod(order);

        return this;
    }

    public NaiveElement sub(Element element) {
        value = value.subtract(((NaiveElement)element).value).mod(order);

        return this;
    }

    public NaiveElement div(Element element) {
        value = value.multiply(((NaiveElement)element).value.modInverse(order)).mod(order);

        return this;
    }

    public NaiveElement mul(Element element) {
        value = value.multiply(((NaiveElement)element).value).mod(order);

        return this;
    }

    public NaiveElement mul(BigInteger n) {
        this.value = this.value.multiply(n).mod(order);

        return this;
    }

    public NaiveElement mulZn(Element z) {
        this.value = this.value.multiply(z.toBigInteger()).mod(order);

        return this;
    }

    public boolean isSqr() {
        return BigInteger.ZERO.equals(value) || BigIntegerUtils.legendre(value, order) == 1;
    }

    public NaiveElement sqrt() {
        // Apply the Tonelli-Shanks Algorithm

        Element e0 = field.newElement();
        Element nqr = field.getNqr();
        Element gInv = nqr.duplicate().invert();

        // let q be the order of the field
        // q - 1 = 2^s t, for some t odd
        BigInteger t = order.subtract(BigInteger.ONE);
        int s = BigIntegerUtils.scanOne(t, 0);
        t = t.divide(BigInteger.valueOf(2 << (s - 1)));

        BigInteger e = BigInteger.ZERO;
        BigInteger orderMinusOne = order.subtract(BigInteger.ONE);

        for (int i = 2; i <= s; i++) {
            e0.set(gInv).pow(e);
            e0.mul(this).pow(orderMinusOne.divide(BigInteger.valueOf(2 << (i - 1))));

            if (!e0.isOne())
                e = e.setBit(i - 1);
        }
        e0.set(gInv).pow(e);
        e0.mul(this);
        t = t.add(BigInteger.ONE);
        t = t.divide(BigIntegerUtils.TWO);
        e = e.divide(BigIntegerUtils.TWO);

        // TODO:
        // (suggested by Hovav Shacham) replace next three lines with
        //  element_pow2_mpz(x, e0, t, nqr, e);
        // once sliding windows are implemented for pow2

        e0.pow(t);
        set(nqr).pow(e).mul(e0);

        return this;
    }


    public NaiveElement pow(BigInteger n) {
        this.value = this.value.modPow(n, order);

        return this;
    }

    public NaiveElement powZn(Element n) {
        return pow(n.toBigInteger());
    }

    public boolean isEqual(Element e) {
        return value.compareTo(((NaiveElement) e).value) == 0;
    }

    public BigInteger toBigInteger() {
        return value;
    }

    @Override
    public byte[] toBytes() {
        byte[] bytes = value.toByteArray();
        if (bytes.length > field.getLengthInBytes()) {
            if (bytes[0] == 0 && bytes.length == field.getLengthInBytes() + 1) {
                // Remove it
                bytes = Arrays.copyOfRange(bytes, 1, bytes.length);
            } else
                throw new IllegalStateException("result has more than FixedLengthInBytes.");
        } else if (bytes.length < field.getLengthInBytes()) {
            byte[] result = new byte[field.getLengthInBytes()];
            System.arraycopy(bytes, 0, result, field.getLengthInBytes() - bytes.length, bytes.length);
            return result;
        }
        return bytes;
    }

    public int sign() {
        if (isZero())
            return 0;

        if (oddOrder) {
            return BigIntegerUtils.isOdd(value) ? 1 : -1;
        } else {
            return value.add(value).compareTo(order);
        }
    }


    public String toString() {
        return (value != null) ? value.toString() : "<null>";
    }

    public boolean equals(Object o) {
        return isEqual((NaiveElement) o);
    }

}
