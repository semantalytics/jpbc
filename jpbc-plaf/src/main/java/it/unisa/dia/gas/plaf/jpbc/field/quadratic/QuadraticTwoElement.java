package it.unisa.dia.gas.plaf.jpbc.field.quadratic;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.field.generic.GenericPointElement;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class QuadraticTwoElement extends GenericPointElement {

    protected QuadraticTwoField field;
    protected Element e0, e1, e2;



    public QuadraticTwoElement(QuadraticTwoField field) {
        super(field);
        this.field = field;

        this.x = field.getTargetField().newElement();
        this.y = field.getTargetField().newElement();

        this.e0 = field.getTargetField().newElement();
        this.e1 = field.getTargetField().newElement();
        this.e2 = field.getTargetField().newElement();
    }

    public QuadraticTwoElement(QuadraticTwoElement element) {
        super(element.field);
        this.field = element.field;

        this.x = element.x.duplicate();
        this.y = element.y.duplicate();

        this.e0 = field.getTargetField().newElement();
        this.e1 = field.getTargetField().newElement();
        this.e2 = field.getTargetField().newElement();
    }


    public QuadraticTwoElement duplicate() {
        return new QuadraticTwoElement(this);
    }

    public QuadraticTwoElement set(Element e) {
        QuadraticTwoElement element = (QuadraticTwoElement) e;

//        this.field = element.field;

        this.x.set(element.x);
        this.y.set(element.y);

        return this;
    }

    public QuadraticTwoElement set(int value) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public QuadraticTwoElement set(BigInteger value) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public boolean isZero() {
        return x.isZero() && y.isZero();
    }

    public boolean isOne() {
        return x.isOne() && y.isZero();
    }

    public QuadraticTwoElement twice() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public QuadraticTwoElement mul(int value) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public QuadraticTwoElement setToZero() {
        x.setToZero();
        y.setToZero();

        return this;
    }

    public QuadraticTwoElement setToOne() {
        x.setToOne();
        y.setToZero();

        return this;
    }

    public QuadraticTwoElement setToRandom() {
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

    public QuadraticTwoElement square() {
        e0.set(x).add(y).mul(e1.set(x).sub(y));
        e1.set(x).mul(y).twice()/*add(e1)*/;

        x.set(e0);
        y.set(e1);

        return this;

        /*
        fq_data_ptr p = a->data;
        fq_data_ptr r = n->data;
        element_t e0, e1;

        element_init(e0, p->x->field);
        element_init(e1, e0->field);
        //Re(n) = x^2 - y^2 = (x+y)(x-y)
        element_add(e0, p->x, p->y);
        element_sub(e1, p->x, p->y);
        element_mul(e0, e0, e1);
        //Im(n) = 2xy
        element_mul(e1, p->x, p->y);
        element_add(e1, e1, e1);
        element_set(r->x, e0);
        element_set(r->y, e1);
        element_clear(e0);
        element_clear(e1);
         */
    }

    public QuadraticTwoElement invert() {
        e0.set(x).square().add(e1.set(y).square()).invert();

        x.mul(e0);
        y.mul(e0.negate());

        return this;

        /*
        fq_data_ptr p = a->data;
        fq_data_ptr r = n->data;
        element_t e0, e1;

        element_init(e0, p->x->field);
        element_init(e1, e0->field);
        element_square(e0, p->x);
        element_square(e1, p->y);
        element_add(e0, e0, e1);
        element_invert(e0, e0);
        element_mul(r->x, p->x, e0);
        element_neg(e0, e0);
        element_mul(r->y, p->y, e0);

        element_clear(e0);
        element_clear(e1);
        */
    }

    public QuadraticTwoElement halve() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public QuadraticTwoElement negate() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public QuadraticTwoElement add(Element e) {
        QuadraticTwoElement element = (QuadraticTwoElement) e;

        x.add(element.x);
        y.add(element.y);

        return this;
    }

    public QuadraticTwoElement sub(Element e) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public QuadraticTwoElement div(Element e) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public QuadraticTwoElement mul(Element e) {
        QuadraticTwoElement element = (QuadraticTwoElement) e;

        e0.set(x).add(y);
        e1.set(element.x).add(element.y);
        e2.set(e0).mul(e1);

        e0.set(x).mul(element.x);
        e1.set(y).mul(element.y);
        e2.sub(e0);

        x.set(e0).sub(e1);
        y.set(e2).sub(e1);

        /*
        fq_data_ptr p = a->data;
        fq_data_ptr q = b->data;
        fq_data_ptr r = n->data;
        element_t e0, e1, e2;

        element_init(e0, p->x->field);
        element_init(e1, e0->field);
        element_init(e2, e0->field);
        Naive way
        element_mul(e0, p->x, q->x);
        element_mul(e1, p->y, q->y);
        element_sub(e0, e0, e1);
        element_mul(e1, p->x, q->y);
        element_mul(e2, p->y, q->x);
        element_add(e1, e1, e2);
        element_set(r->x, e0);
        element_set(r->y, e1);

        //Karatsuba:
        element_add(e0, p->x, p->y);
        element_add(e1, q->x, q->y);
        element_mul(e2, e0, e1);
        element_mul(e0, p->x, q->x);
        element_sub(e2, e2, e0);
        element_mul(e1, p->y, q->y);
        element_sub(r->x, e0, e1);
        element_sub(r->y, e2, e1);

        element_clear(e0);
        element_clear(e1);
        element_clear(e2);
        */
        return this;
    }

    public QuadraticTwoElement mul(BigInteger value) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public QuadraticTwoElement mulZn(Element element) {
        throw new IllegalStateException("Not Implemented yet!!!");        
    }

    public boolean isSqr() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public QuadraticTwoElement sqrt() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public int compareTo(Element e) {
        QuadraticTwoElement element = (QuadraticTwoElement) e;

        return x.compareTo(element.x) ==0 && y.compareTo(element.y) == 0 ? 0 : 1;
    }

    public QuadraticTwoElement powZn(Element element) {
        pow(element.toBigInteger());

        return this;
    }

    public BigInteger toBigInteger() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public QuadraticTwoElement setFromHash(byte[] hash) {
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

}
