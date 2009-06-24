package it.unisa.dia.gas.plaf.jpbc.field.poly;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.field.generic.GenericPolyElement;
import it.unisa.dia.gas.plaf.jpbc.field.polymod.PolyModElement;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PolyElement<E extends Element> extends GenericPolyElement<E> {
    protected PolyField field;


    public PolyElement(PolyField field) {
        super(field);
        this.field = field;
    }


    public PolyField getField() {
        return field;
    }

    public PolyElement duplicate() {
        PolyElement duplicated = new PolyElement(field);

        for (Element e : coeff) {
            duplicated.coeff.add(e.duplicate());
        }

        return duplicated;
    }

    public PolyElement set(Element e) {
        PolyElement<E> element = (PolyElement<E>) e;

        ensureCoeffSize(element.coeff.size());

        for (int i = 0; i < coeff.size(); i++) {
            coeff.get(i).set(element.coeff.get(i));
        }

        return this;
    }

    public PolyElement set(int value) {
        ensureCoeffSize(1);
        coeff.get(0).set(value);
        removeLeadingZeroes();

        return this;
    }

    public PolyElement set(BigInteger value) {
        ensureCoeffSize(1);
        coeff.get(0).set(value);
        removeLeadingZeroes();

        return this;
    }

    public PolyElement setToRandom() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public PolyElement setFromHash(byte[] hash) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public int setFromBytes(byte[] bytes) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int setEncoding(byte[] bytes) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public byte[] getDecoding() {
        return new byte[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public PolyElement setToZero() {
        ensureCoeffSize(0);

        return this;
    }

    public boolean isZero() {
        return coeff.size() == 0;
    }

    public PolyElement setToOne() {
        ensureCoeffSize(1);
        coeff.get(0).setToOne();

        return this;
    }

    public boolean isOne() {
        return coeff.size() == 1 && coeff.get(0).isOne();

    }

    public PolyElement twice() {
        for (int i = 0, size = coeff.size(); i < size; i++) {
            coeff.get(i).twice();
        }

        return this;
    }

    public PolyElement invert() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public PolyElement negate() {
        for (int i = 0, size = coeff.size(); i < size; i++) {
            coeff.get(i).negate();
        }

        return this;
    }

    public PolyElement add(Element e) {
        PolyElement<E> element = (PolyElement<E>) e;


        int i, n, n1;
        PolyElement<E> big;

        n = coeff.size();
        n1 = element.coeff.size();
        if (n > n1) {
            big = this;
            n = n1;
            n1 = coeff.size();
        } else {
            big = element;
        }

        ensureCoeffSize(n1);
        for (i = 0; i < n; i++) {
            coeff.get(i).add(element.coeff.get(i));
        }

        for (; i < n1; i++) {
            coeff.get(i).set(big.coeff.get(i));
        }

        removeLeadingZeroes();

        return this;
        /*
        int i, n, n1;
        element_ptr big;

        n = poly_coeff_count(f);
        n1 = poly_coeff_count(g);
        if (n > n1) {
            big = f;
            n = n1;
            n1 = poly_coeff_count(f);
        } else {
            big = g;
        }

        poly_alloc(sum, n1);
        for (i = 0; i < n; i++) {
            element_add(poly_coeff(sum, i), poly_coeff(f, i), poly_coeff(g, i));
        }
        for (; i < n1; i++) {
            element_set(poly_coeff(sum, i), poly_coeff(big, i));
        }
        poly_remove_leading_zeroes(sum);
        */
    }

    public PolyElement sub(Element e) {
        PolyElement<E> element = (PolyElement<E>) e;

        // f io
        // g è l'altro

        int i, n, n1;

        PolyElement<E> big;

        n = coeff.size();
        n1 = element.coeff.size();

        if (n > n1) {
            big = this;
            n = n1;
            n1 = coeff.size();
        } else {
            big = element;
        }

        ensureCoeffSize(n1);

        for (i = 0; i < n; i++) {
            coeff.get(i).sub(element.coeff.get(i));
        }

        for (; i < n1; i++) {
            if (big == this) {
                coeff.get(i).set(big.coeff.get(i));
//                coeff.add((E) big.coeff.get(i).duplicate());
            } else {
                coeff.get(i).set(big.coeff.get(i)).negate();
//                coeff.add((E) big.coeff.get(i).duplicate().negate());
            }
        }
        removeLeadingZeroes();

        return this;
    }

    public PolyElement div(Element e) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public PolyElement mul(Element e) {
        PolyElement<E> element = (PolyElement<E>) e;

        int fcount = coeff.size();
        int gcount = element.coeff.size();
        int i, j, n;
        PolyElement prod;
        Element e0;

        if (fcount == 0 || gcount == 0) {
            setToZero();
            return this;
        }

        prod = field.newElement();
        n = fcount + gcount - 1;
        prod.ensureCoeffSize(n);

        e0 = field.getTargetField().newElement();
        for (i = 0; i < n; i++) {
            Element x = prod.getCoefficient(i);
            x.setToZero();
            for (j = 0; j <= i; j++) {
                if (j < fcount && i - j < gcount) {
                    e0.set(coeff.get(j)).mul(element.coeff.get(i - j));
                    x.add(e0);
                }
            }
        }
        prod.removeLeadingZeroes();
        set(prod);

        return this;

        /*
        static void poly_mul(element_ptr r, element_ptr f, element_ptr g)
        poly_element_ptr pprod;
        poly_element_ptr pf = f - > data;
        poly_element_ptr pg = g - > data;
        poly_field_data_ptr pdp = r - > field - > data;
        int fcount = pf - > coeff - > count;
        int gcount = pg - > coeff - > count;
        int i, j, n;
        element_t prod;
        element_t e0;

        if (!fcount || !gcount) {
            element_set0(r);
            return;
        }
        element_init(prod, r - > field);
        pprod = prod - > data;
        n = fcount + gcount - 1;
        poly_alloc(prod, n);
        element_init(e0, pdp - > field);
        for (i = 0; i < n; i++) {
            element_ptr x = pprod - > coeff - > item[i];
            element_set0(x);
            for (j = 0; j <= i; j++) {
                if (j < fcount && i - j < gcount) {
                    element_mul(e0, pf - > coeff - > item[j], pg - > coeff - > item[i - j]);
                    element_add(x, x, e0);
                }
            }
        }
        poly_remove_leading_zeroes(prod);
        element_set(r, prod);
        element_clear(e0);
        element_clear(prod);
        */
    }

    public PolyElement mul(int value) {
        for (int i = 0, size = coeff.size(); i < size; i++) {
            coeff.get(i).mul(value);
        }

        return this;
    }

    public PolyElement mul(BigInteger value) {
        for (int i = 0, size = coeff.size(); i < size; i++) {
            coeff.get(i).mul(value);
        }

        return this;
    }

    public PolyElement sqrt() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public boolean isSqr() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public int sign() {
        int res = 0;
        for (int i = 0, size = coeff.size(); i < size; i++) {
            res = coeff.get(i).sign();
            if (res != 0)
                break;
        }
        return res;
    }

    public int compareTo(Element e) {
        PolyElement<E> element = (PolyElement<E>) e;

        int n = this.coeff.size();
        int n1 = element.coeff.size();
        if (n != n1)
            return 1;

        for (int i = 0; i < n; i++) {
            if (coeff.get(i).compareTo(element.coeff.get(i)) != 0)
                return 1;
        }
        return 0;
    }

    public BigInteger toBigInteger() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer("[");

        for (Element e : coeff) {
            buffer.append(e).append(", ");
        }
        buffer.append("]");
        return buffer.toString();
    }

    public int getDegree() {
        return coeff.size() - 1;
    }


    public void ensureCoeffSize(int size) {
        int k = coeff.size();
        while (k < size) {
            coeff.add((E) field.getTargetField().newElement());
            k++;
        }
        while (k > size) {
            k--;
            coeff.remove(coeff.size() - 1);
        }
    }

    public void removeLeadingZeroes() {
        int n = coeff.size() - 1;
        while (n >= 0) {
            Element e0 = coeff.get(n);
            if (!e0.isZero())
                return;

            coeff.remove(n);
            n--;
        }
    }

    public PolyElement setFromPolyMod(PolyModElement polyModElement) {
        int i, n = polyModElement.getField().getN();

        ensureCoeffSize(n);
        for (i = 0; i < n; i++) {
            coeff.get(i).set(polyModElement.getCoefficient(i));
        }
        removeLeadingZeroes();

        return this;
    }
}