package it.unisa.dia.gas.plaf.jpbc.gmp.field;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.field.generic.GenericElement;
import it.unisa.dia.gas.plaf.jpbc.gmp.jna.GMPLibrary;
import it.unisa.dia.gas.plaf.jpbc.gmp.jna.GMPLibraryProvider;
import it.unisa.dia.gas.plaf.jpbc.gmp.jna.MPZElementType;
import it.unisa.dia.gas.plaf.jpbc.pbc.jna.PBCLibraryProvider;
import it.unisa.dia.gas.plaf.jpbc.util.BigIntegerUtils;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class MPZElement extends GenericElement {
    static final GMPLibrary gmpLibrary = GMPLibraryProvider.getGmpLibrary();


    protected MPZElementType value;

    protected BigInteger bigIntOrder;
    protected MPZElementType order;
    protected SecureRandom secureRandom;


    public MPZElement(Field field) {
        super(field);

        this.value = new MPZElementType();
        this.value.init();
        this.order = ((MPZField) field).order;
        this.bigIntOrder = field.getOrder();
    }

    public MPZElement(Field field, BigInteger value) {
        super(field);

        this.value = MPZElementType.fromBigInteger(value);
        this.order = ((MPZField) field).order;
        this.bigIntOrder = field.getOrder();
    }

    public MPZElement(MPZElement element) {
        super(element.getField());

        this.value = element.value.duplicate();
        this.order = ((MPZField) field).order;
        this.bigIntOrder = field.getOrder();
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
        gmpLibrary.__gmpz_set(this.value, ((MPZElement) value).value);

        return this;
    }

    public MPZElement set(int value) {
        gmpLibrary.__gmpz_set_ui(this.value, value);
        gmpLibrary.__gmpz_mod(this.value, this.value, order);

        return this;
    }

    public MPZElement set(BigInteger value) {
        gmpLibrary.__gmpz_set_str(this.value, value.toString(), 10);
        gmpLibrary.__gmpz_mod(this.value, this.value, order);

        return this;
    }

    public boolean isZero() {
        return gmpLibrary.__gmpz_cmp_ui(value, 0) == 0;
    }

    public boolean isOne() {
        return gmpLibrary.__gmpz_cmp_ui(value, 1) == 0;
    }

    public MPZElement twice() {
        gmpLibrary.__gmpz_mul_2exp(value, value, 1);
        if (gmpLibrary.__gmpz_cmp(value, order) >= 0) {
            gmpLibrary.__gmpz_sub(value, value, order);
        }

        return this;
    }

    public MPZElement setToZero() {
        gmpLibrary.__gmpz_set_si(value, 0);

        return this;
    }

    public MPZElement setToOne() {
        gmpLibrary.__gmpz_set_si(value, 1);

        return this;
    }

    public MPZElement setToRandom() {
        if (secureRandom == null)
            secureRandom = new SecureRandom();

        set(new BigInteger(bigIntOrder.bitLength(), secureRandom));

        return this;
    }

    public MPZElement setFromHash(byte[] source, int offset, int length) {
        int i = 0, n, count = (bigIntOrder.bitLength() + 7) / 8;

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

        while (z.compareTo(bigIntOrder) > 0) {
            z = z.divide(BigIntegerUtils.TWO);
        }

        throw new IllegalStateException("Not implemented Yet!!!");
    }

    public int setFromBytes(byte[] source) {
        return setFromBytes(source, 0);
    }

    public int setFromBytes(byte[] source, int offset) {
        throw new IllegalStateException("Not implemented Yet!!!");
    }

    public MPZElement square() {
        gmpLibrary.__gmpz_powm_ui(value, value, 2, order);

        return this;
    }

    public MPZElement invert() {
        gmpLibrary.__gmpz_invert(value, value, order);

        return this;
    }

    public MPZElement halve() {
        mul(new MPZElement(field).set(2).invert());
//
//        if (gmpLibrary.__gmpz_odd_p(value) != 0) {
//            gmpLibrary.__gmpz_add(value, value, order);
//            gmpLibrary.__gmpz_tdiv_q_2exp(value, value, 1);
//        } else {
//            gmpLibrary.__gmpz_tdiv_q_2exp(value, value, 1);
//        }
//
        return this;
    }

    public MPZElement negate() {
        if (isZero()) {
            gmpLibrary.__gmpz_set_ui(value, 0);
        } else {
            gmpLibrary.__gmpz_sub(value, order, value);
        }
        return this;
    }

    public MPZElement add(Element element) {
        gmpLibrary.__gmpz_add(value, value, ((MPZElement) element).value);

        if (gmpLibrary.__gmpz_cmp(value, order) >= 0) {
            gmpLibrary.__gmpz_sub(value, value, order);
        }

        return this;
    }

    public MPZElement sub(Element element) {
        gmpLibrary.__gmpz_sub(value, value, ((MPZElement) element).value);
        if (PBCLibraryProvider.getPbcLibrary().gmp_mpz_sign(value) < 0) {
            gmpLibrary.__gmpz_add(value, value, order);
        }

        return this;
    }

    public MPZElement div(Element element) {
        mul(element.duplicate().invert());

        return this;
    }

    public MPZElement mul(int z) {
        gmpLibrary.__gmpz_mul_si(value, value, z);
        gmpLibrary.__gmpz_mod(value, value, order);

        return this;
    }

    public MPZElement mul(Element element) {
        gmpLibrary.__gmpz_mul(value, value, ((MPZElement) element).value);
        gmpLibrary.__gmpz_mod(value, value, order);

        return this;
    }

    public MPZElement mul(BigInteger n) {
//        this.value = this.value.multiply(n).mod(order);

        throw new IllegalStateException("Not implemented Yet!!!");
    }

    public MPZElement mulZn(Element z) {
        return mul(z);
    }

    public boolean isSqr() {
        if (gmpLibrary.__gmpz_cmp_ui(value, 0) == 0)
            return true;
        return gmpLibrary.__gmpz_legendre(value, order) == 1;
    }

    public MPZElement sqrt() {
        // Apply the Tonelli-Shanks Algorithm

        Element e0 = field.newElement();
        Element nqr = field.getNqr();
        Element gInv = nqr.duplicate().invert();

        // let q be the order of the field
        // q - 1 = 2^s t, for some t odd
        BigInteger t = bigIntOrder.subtract(BigInteger.ONE);
        int s = BigIntegerUtils.scanOne(t, 0);
        t = t.divide(BigInteger.valueOf(2 << (s - 1)));

        BigInteger e = BigInteger.ZERO;
        BigInteger orderMinusOne = bigIntOrder.subtract(BigInteger.ONE);

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
        gmpLibrary.__gmpz_powm(value, value, MPZElementType.fromBigInteger(n), order);

        return this;
    }

    public MPZElement powZn(Element n) {
        return pow(n.toBigInteger());
    }

    public boolean isEqual(Element e) {
        return this == e || gmpLibrary.__gmpz_cmp(value, ((MPZElement) e).value) == 0;
    }

    public BigInteger toBigInteger() {
        return value.toBigInteger();
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
        throw new IllegalStateException("Not implemented Yet!!!");
    }

    public int sign() {
        throw new IllegalStateException("Not implemented Yet!!!");
    }


    public String toString() {
        return value.toString(10);
    }

    public boolean equals(Object o) {
        if (o instanceof MPZElement)
            return isEqual((Element) o);
        return isEqual((MPZElement) o);
    }
}
