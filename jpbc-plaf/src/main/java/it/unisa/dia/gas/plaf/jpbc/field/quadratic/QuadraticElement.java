package it.unisa.dia.gas.plaf.jpbc.field.quadratic;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.field.generic.GenericPointElement;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class QuadraticElement<E extends Element> extends GenericPointElement<E> {

    protected QuadraticField field;

    
    public QuadraticElement(QuadraticField field) {
        super(field);
        this.field = field;

        this.x = (E) field.getTargetField().newElement();
        this.y = (E) field.getTargetField().newElement();
    }

    public QuadraticElement(QuadraticElement element) {
        super(element.field);
        this.field = element.field;

        this.x = (E) element.x.duplicate();
        this.y = (E) element.y.duplicate();
    }


    public QuadraticField getField() {
        return field;
    }

    public QuadraticElement duplicate() {
        return new QuadraticElement(this);
    }

    public QuadraticElement set(Element e) {
        QuadraticElement element = (QuadraticElement) e;

        this.field = element.field;

        this.x = (E) element.x.duplicate();
        this.y = (E) element.y.duplicate();

        return this;
    }

    public QuadraticElement set(int value) {
        x.set(value);
        y.setToZero();

        return this;
    }

    public QuadraticElement set(BigInteger value) {
        x.set(value);
        y.setToZero();

        return this;
    }

    public boolean isZero() {
        return x.isZero() && y.isZero();
    }

    public boolean isOne() {
        return x.isOne() && y.isZero();
    }

    public QuadraticElement setToZero() {
        x.setToZero();
        y.setToZero();

        return this;
    }

    public QuadraticElement setToOne() {
        x.setToOne();
        y.setToZero();

        return this;
    }

    public QuadraticElement setToRandom() {
        x.setToRandom();
        y.setToRandom();

        return this;
    }

    public int setFromBytes(byte[] source, int offset) {
        int len;

        len = x.setFromBytes(source, offset);
        len += y.setFromBytes(source, offset + len);

        return len;
    }

    public int setEncoding(byte[] bytes) {
        int len;

        len = x.setEncoding(bytes);
        y.setToRandom();

        return len;
    }

    public QuadraticElement twice() {
        x.twice();
        y.twice();

        return this;
    }

    public QuadraticElement mul(int z) {
        x.mul(z);
        y.mul(z);

        return this;
    }

    public byte[] getDecoding() {
        return x.getDecoding();
    }

    public QuadraticElement square() {
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

    public QuadraticElement invert() {
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

    public QuadraticElement negate() {
        x.negate();
        y.negate();

        return this;
    }

    public QuadraticElement add(Element e) {
        QuadraticElement element = (QuadraticElement) e;

        x.add(element.x);
        y.add(element.y);

        return this;
    }

    public QuadraticElement sub(Element e) {
        QuadraticElement element = (QuadraticElement) e;

        x.sub(element.x);
        y.sub(element.y);

        return this;
    }

    public QuadraticElement mul(Element e) {
        QuadraticElement element = (QuadraticElement) e;

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

    public QuadraticElement mul(BigInteger n) {
        x.mul(n);
        y.mul(n);

        return this;
    }

    public QuadraticElement mulZn(Element e) {
        x.mulZn(e);
        y.mulZn(e);

        return this;
    }

    public boolean isSqr() {
        Element e0 = x.duplicate().square();
        Element e1 = y.duplicate().square();
        e1.mul(field.getNqr());
        e0.sub(e1);

        return e0.isSqr();

/*
        //x + y sqrt(nqr) is a square iff x^2 - nqr y^2 is (in the base field)
        fq_data_ptr p = e->data;
        element_t e0, e1;
        element_ptr nqr = fq_nqr(e->field);
        int result;
        element_init(e0, p->x->field);
        element_init(e1, e0->field);
        element_square(e0, p->x);
        element_square(e1, p->y);
        element_mul(e1, e1, nqr);
        element_sub(e0, e0, e1);
        result = element_is_sqr(e0);
        element_clear(e0);
        element_clear(e1);
        return result;
*/
    }

    public QuadraticElement sqrt() {
        Element e0 = x.duplicate().square();
        Element e1 = y.duplicate().square();
        e1.mul(field.getNqr());
        e0.sub(e1);
        e0.sqrt();
        e1.set(x).add(e0);

        Element e2 = x.getField().newElement().set(2).invert();
        e1.mul(e2);

        if (!e1.isSqr())
            e1.sub(e0);

        e0.set(e1).sqrt();
        e1.set(e0).add(e0);
        e1.invert();
        y.mul(e1);
        x.set(e0);

        return this;
        /*
        fq_data_ptr p = e->data;
        fq_data_ptr r = n->data;
        element_ptr nqr = fq_nqr(n->field);
        element_t e0, e1, e2;

        //if (a+b sqrt(nqr))^2 = x+y sqrt(nqr) then
        //2a^2 = x +- sqrt(x^2 - nqr y^2)
        //(take the sign which allows a to exist)
        //and 2ab = y
        element_init(e0, p->x->field);
        element_init(e1, e0->field);
        element_init(e2, e0->field);
        element_square(e0, p->x);
        element_square(e1, p->y);
        element_mul(e1, e1, nqr);
        element_sub(e0, e0, e1);
        element_sqrt(e0, e0);
        //e0 = sqrt(x^2 - nqr y^2)
        element_add(e1, p->x, e0);
        element_set_si(e2, 2);
        element_invert(e2, e2);
        element_mul(e1, e1, e2);
        //e1 = (x + sqrt(x^2 - nqr y^2))/2
        if (!element_is_sqr(e1)) {
        element_sub(e1, e1, e0);
        //e1 should be a square
        }
        element_sqrt(e0, e1);
        element_add(e1, e0, e0);
        element_invert(e1, e1);
        element_mul(r->y, p->y, e1);
        element_set(r->x, e0);
        element_clear(e0);
        element_clear(e1);
        element_clear(e2);
        */
    }

    public boolean isEqual(Element e) {
        if (e == this)
            return true;
        
        QuadraticElement element = (QuadraticElement) e;

        return x.isEqual(element.x) && y.isEqual(element.y);
    }

    public QuadraticElement powZn(Element n) {
        pow(n.toBigInteger());

        return this;
    }

    public BigInteger toBigInteger() {
        return x.toBigInteger();
    }

    public byte[] toBytes() {
        byte[] xBytes = x.toBytes();
        byte[] yBytes = y.toBytes();

        byte[] result = new byte[xBytes.length + yBytes.length];
        System.arraycopy(xBytes, 0, result, 0, xBytes.length);
        System.arraycopy(yBytes, 0, result, xBytes.length, yBytes.length);

        return result;
    }

    public QuadraticElement setFromHash(byte[] source, int offset, int length) {
        int k = length / 2;
        x.setFromHash(source, offset, k);
        y.setFromHash(source, offset + k, k);

        return this;
    }

    public int sign() {
        int res = x.sign();
        if (res == 0)
            return y.sign();
        return res;

        /*
        int res;
        fq_data_ptr r = n->data;
        res = element_sign(r->x);
        if (!res) return element_sign(r->y);
        return res;
        */
    }

    public String toString() {
        return String.format("{x=%s,y=%s}", x, y);
    }

}