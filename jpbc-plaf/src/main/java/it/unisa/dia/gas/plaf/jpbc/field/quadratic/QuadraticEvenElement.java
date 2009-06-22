package it.unisa.dia.gas.plaf.jpbc.field.quadratic;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.field.generic.GenericPointElement;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class QuadraticEvenElement extends GenericPointElement {

    protected QuadraticEvenField field;

    
    public QuadraticEvenElement(QuadraticEvenField field) {
        super(field);
        this.field = field;

        this.x = field.getTargetField().newElement();
        this.y = field.getTargetField().newElement();
    }

    public QuadraticEvenElement(QuadraticEvenElement element) {
        super(element.field);
        this.field = element.field;

        this.x = element.x.duplicate();
        this.y = element.y.duplicate();
    }


    public QuadraticEvenField getField() {
        return field;
    }

    public QuadraticEvenElement duplicate() {
        return new QuadraticEvenElement(this);
    }

    public QuadraticEvenElement set(Element e) {
        QuadraticEvenElement element = (QuadraticEvenElement) e;

        this.field = element.field;

        this.x = element.x.duplicate();
        this.y = element.y.duplicate();

        return this;
    }

    public QuadraticEvenElement set(int value) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public QuadraticEvenElement set(BigInteger value) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public boolean isZero() {
        return x.isZero() && y.isZero();
    }

    public boolean isOne() {
        return x.isOne() && y.isZero();
    }

    public QuadraticEvenElement twice() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public QuadraticEvenElement mul(int value) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public QuadraticEvenElement setToZero() {
        x.setToZero();
        y.setToZero();

        return this;
    }

    public QuadraticEvenElement setToOne() {
        x.setToOne();
        y.setToZero();

        return this;
    }

    public QuadraticEvenElement setToRandom() {
        x.setToRandom();
        y.setToRandom();

        return this;
    }

    public int setFromBytes(byte[] bytes) {
        int len;

        len = x.setFromBytes(bytes);
        len += y.setFromBytes(Arrays.copyOfRange(bytes, len, bytes.length - len));

        return len;
    }

    public int setEncoding(byte[] bytes) {
        int len;

        len = x.setEncoding(bytes);
        y.setToRandom();

        return len;
    }

    public byte[] getDecoding() {
        return x.getDecoding();
    }

    public QuadraticEvenElement square() {
//        System.out.println("fq_square (a) = " + this);

        Element e0 = x.duplicate().square();
        Element e1 = y.duplicate().square();
        e1.mul(field.getTargetField().getNqr());
        e0.add(e1);
        e1.set(x).mul(y);
        e1.twice();
        x.set(e0);
        y.set(e1);

        return this;
        /*
            fq_data_ptr p = a->data;
            fq_data_ptr r = n->data;
            element_ptr nqr = fq_nqr(n->field);
            element_t e0, e1;

            element_init(e0, p->x->field);
            element_init(e1, e0->field);
            element_square(e0, p->x);
            element_square(e1, p->y);
            element_mul(e1, e1, nqr);
            element_add(e0, e0, e1);
            element_mul(e1, p->x, p->y);
            //TODO: which is faster?
            //element_add(e1, e1, e1);
            element_double(e1, e1);
            element_set(r->x, e0);
            element_set(r->y, e1);
            element_clear(e0);
            element_clear(e1);
         */
    }

    public QuadraticEvenElement invert() {
        Element e0 = x.duplicate().square();
        Element e1 = y.duplicate().square();
        e1.mul(field.getTargetField().getNqr());
        e0.sub(e1);
        e0.invert();
        x.mul(e0);
        e0.negate();
        y.mul(e0);

        return this;
        /*
            fq_data_ptr p = a->data;
            fq_data_ptr r = n->data;
            element_ptr nqr = fq_nqr(n->field);
            element_t e0, e1;

            element_init(e0, p->x->field);
            element_init(e1, e0->field);
            element_square(e0, p->x);
            element_square(e1, p->y);
            element_mul(e1, e1, nqr);
            element_sub(e0, e0, e1);
            element_invert(e0, e0);
            element_mul(r->x, p->x, e0);
            element_neg(e0, e0);
            element_mul(r->y, p->y, e0);

            element_clear(e0);
            element_clear(e1);
        */
    }

    public QuadraticEvenElement halve() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public QuadraticEvenElement negate() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public QuadraticEvenElement add(Element e) {
        QuadraticEvenElement element = (QuadraticEvenElement) e;

        x.add(element.x);
        y.add(element.y);

        return this;
    }

    public QuadraticEvenElement sub(Element e) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public QuadraticEvenElement div(Element e) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public QuadraticEvenElement mul(Element e) {
        QuadraticEvenElement element = (QuadraticEvenElement) e;

//        System.out.println("mul (a) = " + this);
//        System.out.println("mul (b) = " + element);

//        System.out.println("nqr = " + field.targetField.getNqr());
        Element e0 = x.duplicate().add(y);
        Element e1 = element.x.duplicate().add(element.y);
        Element e2 = e0.duplicate().mul(e1);

//        System.out.println("e0 = " + e0);
//        System.out.println("e1 = " + e1);
//        System.out.println("e2 = " + e2);

        e0.set(x).mul(element.x);
        e1.set(y).mul(element.y);
        
//        System.out.println("e0 = " + e0);
//        System.out.println("e1 = " + e1);

        x.set(e1).mul(field.getTargetField().getNqr()).add(e0);
        e2.sub(e0);
        y.set(e2).sub(e1);

//        System.out.println("mul (n) = " + this);
        
        return this;
        /*
        fq_data_ptr p = a->data;    // element
        fq_data_ptr q = b->data;
        fq_data_ptr r = n->data;

        element_ptr nqr = fq_nqr(n->field);
        element_t e0, e1, e2;

        element_init(e0, p->x->field);
        element_init(e1, e0->field);
        element_init(e2, e0->field);
        //Karatsuba:
        element_add(e0, p->x, p->y);
        element_add(e1, q->x, q->y);
        element_mul(e2, e0, e1);
        element_mul(e0, p->x, q->x);
        element_mul(e1, p->y, q->y);
        element_mul(r->x, e1, nqr);
        element_add(r->x, r->x, e0);
        element_sub(e2, e2, e0);
        element_sub(r->y, e2, e1);

        element_clear(e0);
        element_clear(e1);
        element_clear(e2);
        */
    }

    public QuadraticEvenElement mul(BigInteger value) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public QuadraticEvenElement mulZn(Element element) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public boolean isSqr() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public QuadraticEvenElement sqrt() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public int compareTo(Element e) {
        QuadraticEvenElement element = (QuadraticEvenElement) e;

        return x.compareTo(element.x) ==0 && y.compareTo(element.y) == 0 ? 0 : 1;
    }

    public QuadraticEvenElement powZn(Element element) {
        pow(element.toBigInteger());

        return this;
    }

    public BigInteger toBigInteger() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public QuadraticEvenElement setFromHash(byte[] hash) {
        int k = hash.length / 2;
        x.setFromHash(Arrays.copyOf(hash, k));
        y.setFromHash(Arrays.copyOfRange(hash, k, hash.length - k));

        return this;
    }

    public int sign() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public String toString() {
        return String.format("{x=%s,y=%s}", x, y);
    }


    public Element getX() {
        return x;
    }

    public Element getY() {
        return y;
    }


}