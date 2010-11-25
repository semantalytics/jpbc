package it.unisa.dia.gas.plaf.jpbc.gmp.field;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.field.generic.GenericElement;
import it.unisa.dia.gas.plaf.jpbc.gmp.jna.GMPLibrary;
import it.unisa.dia.gas.plaf.jpbc.gmp.jna.GMPLibraryProvider;
import it.unisa.dia.gas.plaf.jpbc.gmp.jna.MPZElementType;
import it.unisa.dia.gas.plaf.jpbc.util.BigIntegerUtils;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class MPZElement extends GenericElement {
    static final GMPLibrary gmpLibrary = GMPLibraryProvider.getGmpLibrary();


    protected MPZElementType value;

    protected BigInteger order;
    protected SecureRandom secureRandom;


    public MPZElement(Field field) {
        super(field);

        this.value = new MPZElementType();
        this.value.init();
        this.order = field.getOrder();
    }

    public MPZElement(Field field, BigInteger value) {
        super(field);

        this.value = MPZElementType.fromBigInteger(value);
        this.order = field.getOrder();
    }

    public MPZElement(MPZElement element) {
        super(element.getField());

        this.value = element.value.duplicate();
        this.order = element.field.getOrder();
    }


    @Override
    public Element getImmutable() {
        //return new ImmutableGMPNaiveElement(this);
        throw new IllegalStateException("Not implemented Yet!!!");
    }

    public MPZElement duplicate() {
        return new MPZElement(this);
    }

    public MPZElement set(Element value) {
            // TODO: we should import also the field...
        //if (this.field != value.getField()) {
//        this.field = element.field;
        //}
        this.value = ((MPZElement) value).value.duplicate();

        return this;
    }

    public MPZElement set(int value) {
//        gmpLibrary.__gmpz_set(this.value, value);
//        gmpLibrary.__gmpz_mod(this.value);

        return this;
    }

    public MPZElement set(BigInteger value) {
        gmpLibrary.__gmpz_set_str(this.value, value.toString(), 10);
//        gmpLibrary.__gmpz_mod(order);

        return this;
    }

    public boolean isZero() {
        return BigInteger.ZERO.equals(value);
    }

    public boolean isOne() {
        return BigInteger.ONE.equals(value);
    }

    public MPZElement twice() {
//        gmpLibrary.__gmpz_add(value, value);
//        gmpLibrary.__gmpz_mod(order);

        return this;
    }

    public MPZElement mul(int z) {
//        gmpLibrary.__gmpz_mul(value, z);
//        gmpLibrary.__gmpz_mod(order);

        return this;
    }

    public MPZElement setToZero() {
//        this.value = BigInteger.ZERO;

        return this;
    }

    public MPZElement setToOne() {
//        this.value = BigInteger.ONE;

        return this;
    }

    public MPZElement setToRandom() {
        if (secureRandom == null)
            secureRandom = new SecureRandom();

//        this.value = new BigInteger(order.bitLength(), secureRandom).mod(order);

        return this;
    }

    public MPZElement setFromHash(byte[] source, int offset, int length) {
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
        BigInteger z = new BigInteger(1, buf);

        while (z.compareTo(order) > 0) {
            z = z.divide(BigIntegerUtils.TWO);
        }

//        this.value = z;

        return this;
    }

    public int setFromBytes(byte[] source) {
        return setFromBytes(source, 0);
    }

    public int setFromBytes(byte[] source, int offset) {
//        value = new BigInteger(1, Utils.copyOf(source, offset, field.getLengthInBytes())).mod(order);

        return field.getLengthInBytes();
    }

    public MPZElement square() {
//        value = value.modPow(BigIntegerUtils.TWO, order);
//        value = value.multiply(value).mod(order);

        return this;
    }

    public MPZElement invert() {
//        value = value.modInverse(order);

        return this;
    }

    public MPZElement halve() {
//        value = value.multiply(BigIntegerUtils.TWO.modInverse(order)).mod(order);

        return this;
    }

    public MPZElement negate() {
        if (isZero()) {
//            value = BigInteger.ZERO;
            return this;
        }

//        value = order.subtract(value);

        return this;
    }

    public MPZElement add(Element element) {
//        gmpLibrary.__gmpz_add(value, ((MPZElement)element).value);
//        gmpLibrary.__gmpz_mod(value);

        return this;
    }

    public MPZElement sub(Element element) {
//        gmpLibrary.__gmpz_sub(value, ((MPZElement)element).value);
//        gmpLibrary.__gmpz_mod(value);

        return this;
    }

    public MPZElement div(Element element) {
//        value = value.multiply(((MPZElement)element).value.modInverse(order)).mod(order);

        return this;
    }

    public MPZElement mul(Element element) {
//        gmpLibrary.__gmpz_mul(value, ((MPZElement)element).value);
//        gmpLibrary.__gmpz_mod(value);

        return this;
    }

    public MPZElement mul(BigInteger n) {
//        this.value = this.value.multiply(n).mod(order);

        return this;
    }

    public MPZElement mulZn(Element z) {
        return mul(z);
    }

    public boolean isSqr() {
//        return BigInteger.ZERO.equals(value) || BigIntegerUtils.legendre(value, order) == 1;
        return false;
    }

    public MPZElement sqrt() {
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


    public MPZElement pow(BigInteger n) {
//        this.value = this.value.modPow(n, order);

        return this;
    }

    public MPZElement powZn(Element n) {
        return pow(n.toBigInteger());
    }

    public boolean isEqual(Element e) {
//        return this == e || value.compareTo(((MPZElement) e).value) == 0;
        return false;

    }

    public BigInteger toBigInteger() {
        return new BigInteger(value.toString(10));
    }

    @Override
    public byte[] toBytes() {
//        byte[] bytes = value.toByteArray();
//
//        if (bytes.length > field.getLengthInBytes()) {
//            if (bytes[0] == 0 && bytes.length == field.getLengthInBytes() + 1) {
//                // Remove it
//                bytes = BigIntegerUtils.copyOfRange(bytes, 1, bytes.length);
//            } else
//                throw new IllegalStateException("result has more than FixedLengthInBytes.");
//        } else if (bytes.length < field.getLengthInBytes()) {
//            byte[] result = new byte[field.getLengthInBytes()];
//            System.arraycopy(bytes, 0, result, field.getLengthInBytes() - bytes.length, bytes.length);
//            return result;
//        }
//        return bytes;
        return null;
    }

    public int sign() {
        if (isZero())
            return 0;

//        if (field.isOrderOdd()) {
//            return BigIntegerUtils.isOdd(value) ? 1 : -1;
//        } else {
//            return value.add(value).compareTo(order);
//        }
        return 0;
    }


    public String toString() {
        return (value != null) ? value.toString() : "<null>";
    }

    public boolean equals(Object o) {
        if (o instanceof MPZElement)
            return isEqual((Element) o);        
        return isEqual((MPZElement) o);
    }
}
