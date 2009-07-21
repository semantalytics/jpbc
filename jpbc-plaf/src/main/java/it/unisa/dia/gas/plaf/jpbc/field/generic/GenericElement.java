package it.unisa.dia.gas.plaf.jpbc.field.generic;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public abstract class GenericElement implements Element {
    protected Field field;
    protected GenericPowPreProcessing powPreProcessing;


    public GenericElement(Field field) {
        this.field = field;
    }


    public Field getField() {
        return field;
    }

    public int getLengthInBytes() {
        return field.getFixedLengthInBytes();
    }

    public int setFromBytes(byte[] source) {
        return setFromBytes(source, 0);
    }

    public int setFromBytes(byte[] source, int offset) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public Element pow(BigInteger n) {
        if (BigInteger.ZERO.equals(n)) {
            setToOne();
            return this;
        }

        elementPowWind(n);

        return this;
    }

    public Element powZn(Element n) {
        return pow(n.toBigInteger());
    }

    public Element halve() {
        return mul(field.newElement().set(2).invert());
    }

    public Element sub(Element element) {
        add(element.duplicate().negate());
        return this;

        /*
        if (c != a) {
            element_neg(c, b);
            element_add(c, c, a);
        } else {
            element_t tmp;
            element_init(tmp, a - > field);
            element_neg(tmp, b);
            element_add(c, tmp, a);
            element_clear(tmp);
        }
        */
    }

    public Element div(Element element) {
        return mul(element.duplicate().invert());
    }

    public Element mul(int z) {
        mul(field.newElement().set(z));

        return this;

        /*
        element_t e0;
        element_init(e0, r->field);
        element_set_si(e0, n);
        element_mul(r, a, e0);
        element_clear(e0);
        */
    }

    public Element sqrt() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public byte[] toBytes() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public Element mulZn(Element z) {
        return mul(z.toBigInteger());
    }

    public Element square() {
        return mul(this);
    }

    public Element twice() {
        return add(this);
    }

    public int setEncoding(byte[] bytes) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public byte[] getDecoding() {
        throw new IllegalStateException("Not implemented yet!!!");
    }


    public void initPowPreProcessing() {
        powPreProcessing = getPowPreProcessing(field.getOrder().bitLength(), 5);
    }

    public Element powPreProcessing(BigInteger n) {
        if (powPreProcessing == null)
            throw new IllegalStateException("You must call setPowPreProcessing before this.");

        element_pow_base_table(n, powPreProcessing);

        return this;
    }


    public void element_pow_base_table(BigInteger n, GenericPowPreProcessing base_table) {
        /* early abort if raising to power 0 */
        if (n.signum() == 0) {
            setToOne();
            return;
        }

        Element result = field.newOneElement();
        int numLookups = n.bitLength() / base_table.k + 1;

        for (int row = 0; row < numLookups; row++) {
            int word = 0;
            for (int s = 0; s < base_table.k; s++) {
                word |= (n.testBit(base_table.k * row + s) ? 1 : 0) << s;
            }

            if (word > 0) {
                result.mul(base_table.table[row][word]);
            }

        }

        set(result);
    }


    protected int optimalPowWindowSize(BigInteger n) {
        int expBits;

        expBits = n.bitLength();

        /* try to minimize 2^k + n/(k+1). */
        if (expBits > 9065)
            return 8;
        if (expBits > 3529)
            return 7;
        if (expBits > 1324)
            return 6;
        if (expBits > 474)
            return 5;
        if (expBits > 157)
            return 4;
        if (expBits > 47)
            return 3;
        return 2;
    }

    /**
     * Builds k-bit lookup window for base a
     *
     * @param k
     * @return
     */
    protected List<Element> buildPowWindow(int k) {
        int s;
        int lookupSize;
        List<Element> lookup;

        if (k < 1) {                /* no window */
            return null;
        }

        /* build 2^k lookup table.  lookup[i] = x^i. */
        /* TODO: a more careful word-finding algorithm would allow
         *       us to avoid calculating even lookup entries > 2
         */
        lookupSize = 1 << k;
        lookup = new ArrayList<Element>(lookupSize);

        lookup.add(field.newElement().setToOne());
        for (s = 1; s < lookupSize; s++) {
            lookup.add(lookup.get(s - 1).duplicate().mul(this));
        }

        return lookup;
    }

    /**
     * left-to-right exponentiation with k-bit window.
     * NB. must have k >= 1.
     *
     * @param n
     */
    protected void elementPowWind(BigInteger n) {
        /* early abort if raising to power 0 */
        if (n.signum() == 0) {
            setToOne();
            return;
        }

        int word = 0;               /* the word to look up. 0<word<base */
        int wbits = 0;           /* # of bits so far in word. wbits<=k. */
        int k = optimalPowWindowSize(n);
        List<Element> lookup = buildPowWindow(k);
        Element result = field.newElement().setToOne();

        for (int inword = 0, s = n.bitLength() - 1; s >= 0; s--) {
            result.square();
            int bit = n.testBit(s) ? 1 : 0;

            if (inword == 0 && bit == 0)
                continue;           /* keep scanning.  note continue. */

            if (inword == 0) {          /* was scanning, just found word */
                inword = 1;             /* so, start new word */
                word = 1;
                wbits = 1;
            } else {
                word = (word << 1) + bit;
                wbits++; /* continue word */
            }

            if (wbits == k || s == 0) {
                result.mul(lookup.get(word));
                inword = 0;
            }
        }

        set(result);
    }

    /**
     * build k-bit base table for n-bit exponentiation w/ base a
     *
     * @param bits
     * @param k
     * @return
     */
    protected GenericPowPreProcessing getPowPreProcessing(int bits, int k) {
        int lookup_size = 1 << k;

        GenericPowPreProcessing base_table = new GenericPowPreProcessing();
        base_table.numLookups = bits / k + 1;
        base_table.k = k;
        base_table.bits = bits;
        base_table.table = new Element[base_table.numLookups][lookup_size];

        Element multiplier = duplicate();

        for (int i = 0; i < base_table.numLookups; i++) {

            base_table.table[i][0] = field.newOneElement();

            for (int j = 1; j < lookup_size; j++) {
                base_table.table[i][j] = multiplier.duplicate().mul(base_table.table[i][j - 1]);
            }
            multiplier.mul(base_table.table[i][lookup_size - 1]);
        }

        return base_table;
    }


    public class GenericPowPreProcessing implements PreProcessing {
        int k;
        int bits;
        int numLookups;
        Element table[][];
    }

}
