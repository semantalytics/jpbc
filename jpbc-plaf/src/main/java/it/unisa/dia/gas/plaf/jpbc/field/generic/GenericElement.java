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


    public GenericElement(Field field) {
        this.field = field;
    }


    public Field getField() {
        return field;
    }

    public Element map(Element Element) {
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

    public Element mul(int value) {
        mul(field.newElement().set(value));

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

    public Element mulZn(Element element) {
        return mul(element.toBigInteger());
    }

    public Element square() {
        return mul(this);
    }

    public Element twice() {
        return add(this);
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

}
