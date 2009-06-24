package it.unisa.dia.gas.plaf.jpbc.field.naive;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.field.generic.GenericElement;
import it.unisa.dia.gas.plaf.jpbc.util.BigIntegerUtils;
import it.unisa.dia.gas.plaf.jpbc.util.Utils;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class NaiveElement extends GenericElement {

    public BigInteger value;
    protected BigInteger order;

    protected SecureRandom secureRandom;


    public NaiveElement(Field field) {
        super(field);

        this.value = BigInteger.ZERO;
        this.order = field.getOrder();
    }

    public NaiveElement(Field field, BigInteger value) {
        super(field);

        this.value = value;
        this.order = field.getOrder();
    }


    public NaiveElement duplicate() {
        return new NaiveElement(field, value);
    }

    public NaiveElement set(Element element) {
        // TODO: we should import also the field...
//        this.field = element.field;
        this.value = ((NaiveElement)element).value;

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

    public NaiveElement mul(int value) {
        this.value = this.value.multiply(BigInteger.valueOf(value)).mod(order);

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

    public NaiveElement setFromHash(byte[] hash) {
        int i = 0, n, count = (order.bitLength() + 7) / 8;
        int len = hash.length;

        byte[] buf = new byte[count];
        byte counter = 0;
        boolean done = false;

        for (; ;) {

            if (len >= count - i) {
                n = count - i;
                done = true;
            } else n = len;

            System.arraycopy(hash, 0, buf, i, n);
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

    public int setFromBytes(byte[] bytes) {
        value = new BigInteger(Utils.copyOf(bytes, field.getFixedLengthInBytes())).mod(order);

        return field.getFixedLengthInBytes();
    }

    public int setEncoding(byte[] bytes) {
        byte[] source = Utils.copyOf(bytes, field.getFixedLengthInBytes() - 1);
        value = new BigInteger(source);

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

    public NaiveElement mul(BigInteger value) {
        this.value = this.value.multiply(value).mod(order);

        return this;
    }

    public NaiveElement mulZn(Element element) {
        this.value = this.value.multiply(element.toBigInteger()).mod(order);

        return this;
    }

    public boolean isSqr() {
        return BigInteger.ZERO.equals(value) || BigIntegerUtils.legendre(value, order) == 1;
    }

    public NaiveElement sqrt() {
        int s;
        int i;
        BigInteger e;
        BigInteger t, t0;
        Element ginv, e0;
        Element nqr;

        ginv = field.newElement();
        e0 = field.newElement();
        nqr = field.getNqr();

        ginv.set(nqr).invert();

        //let q be the order of the field
        //q - 1 = 2^s t, t odd
        t = order.subtract(BigInteger.ONE)/*.setMutable(true)*/;
        s = BigIntegerUtils.scanOne(t, 0);
        t = t.divide(BigInteger.valueOf(2 << (s - 1)));

        e = BigInteger.ZERO;

        for (i = 2; i <= s; i++) {
            t0 = order.subtract(BigInteger.ONE);
            t0 = t0.divide(BigInteger.valueOf(2 << (i - 1)));

            e0.set(ginv).pow(e);
            e0.mul(this).pow(t0);

            if (!e0.isOne())
                e = e.setBit(i - 1);
        }
        e0.set(ginv).pow(e);
        e0.mul(this);
        t = t.add(BigInteger.ONE);
        t = t.divide(BigIntegerUtils.TWO);
        e = e.divide(BigIntegerUtils.TWO);

        //(suggested by Hovav Shacham) replace next three lines with
        //  element_pow2_mpz(x, e0, t, nqr, e);
        //once sliding windows are implemented for pow2

        e0.pow(t);
        set(nqr).pow(e).mul(e0);

        return this;
    }


    public NaiveElement pow(BigInteger value) {
        this.value = this.value.modPow(value, order);

        return this;
    }

    public NaiveElement powZn(Element element) {
        return null;
    }

    public int compareTo(Element element) {
        return value.compareTo(((NaiveElement)element).value);
    }

    public BigInteger toBigInteger() {
        return value;
    }

    public int sign() {
        return value.signum();
    }


    public String toString() {
        return (value != null) ? value.toString() : "<null>";
    }

    public byte[] getBytes() {
        return value.toByteArray();

    }
}