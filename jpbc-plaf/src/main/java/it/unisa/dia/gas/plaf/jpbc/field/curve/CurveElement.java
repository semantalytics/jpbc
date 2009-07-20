package it.unisa.dia.gas.plaf.jpbc.field.curve;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.FieldOver;
import it.unisa.dia.gas.plaf.jpbc.field.generic.GenericPointElement;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class CurveElement extends GenericPointElement {
    public int infFlag;

    protected SecureRandom random;


    public CurveElement(FieldOver field) {
        super(field);

        this.x = field.getTargetField().newElement();
        this.y = field.getTargetField().newElement();
        this.infFlag = 1;
    }

    public CurveElement(CurveElement curveElement) {
        super(curveElement.field);

        this.x = curveElement.x.duplicate();
        this.y = curveElement.y.duplicate();
        this.infFlag = curveElement.infFlag;
    }


    public CurveField getField() {
        return (CurveField) field;
    }

    public CurveElement duplicate() {
        return new CurveElement(this);
    }

    public CurveElement set(Element e) {
        CurveElement element = (CurveElement) e;

        if (element.infFlag != 0) {
            infFlag = 1;
            return this;
        }

        this.x.set(element.x);
        this.y.set(element.y);
        this.infFlag = 0;

        return this;
    }

    public CurveElement set(int value) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public CurveElement set(BigInteger value) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public boolean isZero() {
        return infFlag == 1;
    }

    public boolean isOne() {
        return infFlag == 1;
    }

    public CurveElement twice() {
        return mul(this);
    }

    public CurveElement setToZero() {
        infFlag = 1;

        return this;
    }

    public CurveElement setToOne() {
        infFlag = 1;

        return this;
    }

    public CurveElement setToRandom() {
        if (random == null)
            random = new SecureRandom();

        BigInteger order = field.getTargetField().getOrder();
        set(getField().gen).mul(new BigInteger(order.bitLength(), random).mod(order));

        return this;
    }

    public int setFromBytes(byte[] source, int offset) {
        int len;

        infFlag = 0;
        len = x.setFromBytes(source, offset);
        len += y.setFromBytes(source, offset + len);

        //if point does not lie on curve, set it to O
        if (!isValid()) 
            setToZero();

        return len;
    }

    public CurveElement square() {
        if (infFlag != 0) {
            infFlag = 1;
            return this;
        }

        if (y.isZero()) {
            infFlag = 1;
            return this;
        }

        twiceInternal();

        return this;
    }

    public CurveElement invert() {
        if (infFlag != 0) {
            infFlag = 1;
            return this;
        }

        infFlag = 0;
        y.negate();

        return this;

        /*
        point_ptr r = c->data, p = a->data;

        if (p->inf_flag) {
        r->inf_flag = 1;
        return;
        }
        r->inf_flag = 0;
        element_set(r->x, p->x);
        element_neg(r->y, p->y);
        */
    }

    public CurveElement negate() {
        return invert();
    }

    public CurveElement add(Element e) {
        mul(e);

        return this;
    }

    public CurveElement mul(Element e) {
        if (infFlag != 0) {
            set(e);
            return this;
        }

        CurveElement element = (CurveElement) e;

        if (element.infFlag != 0)
            return this;

        if (x.compareTo(element.x) == 0) {

            if (y.compareTo(element.y) == 0) {
                if (y.isZero()) {
                    infFlag = 1;
                    return this;
                } else {
                    twiceInternal();
                    return this;
                }
            }

            infFlag = 1;
            return this;
        } else {
            //lambda = (y2-y1)/(x2-x1)
            Element lambda = element.y.duplicate().sub(y).mul(element.x.duplicate().sub(x).invert());

            //x3 = lambda^2 - x1 - x2
            Element x3 = lambda.duplicate().square().sub(x).sub(element.x);

            //y3 = (x1-x3)lambda - y1
            Element y3 = x.duplicate().sub(x3).mul(lambda).sub(y);

            x.set(x3);
            y.set(y3);
            infFlag = 0;
        }

        return this;

        /*curve_data_ptr cdp = a->field->data;
        point_ptr r = c->data, p = a->data, q = b->data;

        if (p->inf_flag) {
        curve_set(c, b);
        return;
        }
        if (q->inf_flag) {
        curve_set(c, a);
        return;
        }
        if (!element_cmp(p->x, q->x)) {
        if (!element_cmp(p->y, q->y)) {
            if (element_is0(p->y)) {
            r->inf_flag = 1;
            return;
            } else {
            double_no_check(r, p, cdp->a);
            return;
            }
        }
        //points are inverses of each other
        r->inf_flag = 1;
        return;
        } else {
        element_t lambda, e0, e1;

        element_init(lambda, cdp->field);
        element_init(e0, cdp->field);
        element_init(e1, cdp->field);

        //lambda = (y2-y1)/(x2-x1)
        element_sub(e0, q->x, p->x);
        element_invert(e0, e0);
        element_sub(lambda, q->y, p->y);
        element_mul(lambda, lambda, e0);
        //x3 = lambda^2 - x1 - x2
        element_square(e0, lambda);
        element_sub(e0, e0, p->x);
        element_sub(e0, e0, q->x);
        //y3 = (x1-x3)lambda - y1
        element_sub(e1, p->x, e0);
        element_mul(e1, e1, lambda);
        element_sub(e1, e1, p->y);

        element_set(r->x, e0);
        element_set(r->y, e1);
            r->inf_flag = 0;

        element_clear(lambda);
        element_clear(e0);
        element_clear(e1);
        }
        */

    }

    public CurveElement mul(BigInteger n) {
        return (CurveElement) pow(n);
    }

    public CurveElement mulZn(Element e) {
        return powZn(e);
    }

    public boolean isSqr() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public int compareTo(Element e) {
        if (this == e)
            return 0;

        CurveElement element = (CurveElement) e;

        if (infFlag != 0) {
            return element.infFlag;
        }

        return x.compareTo(element.x) == 0 && y.compareTo(element.y) == 0 ? 0 : 1;

        /*
        if (a == b) {
            return 0;
        } else {
            point_ptr p = a - > data;
            point_ptr q = b - > data;
            if (p - > inf_flag) {
                return q - > inf_flag;
            }
            return element_cmp(p - > x, q - > x) || element_cmp(p - > y, q - > y);
        }
        */

    }

    public CurveElement powZn(Element e) {
        pow(e.toBigInteger());
        return this;
    }

    public BigInteger toBigInteger() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public byte[] toBytes() {
        byte[] xBytes = x.toBytes();
        byte[] yBytes = y.toBytes();

        byte[] result = new byte[xBytes.length + yBytes.length];
        System.arraycopy(xBytes, 0, result, 0, xBytes.length);
        System.arraycopy(yBytes, 0, result, xBytes.length, yBytes.length);

        return result;
    }

    public CurveElement setFromHash(byte[] source, int offset, int length) {
/*        //TODO: don't find a hash by the 255th try = freeze!

        char[] datacopy = hash.toCharArray();
        Element t = field.targetField.newElement();

        infFlag = 0;
        for (; ;) {
            x.setFromHash(new String(datacopy));
            t.set(x).square().add(field.a).mul(x).add(field.b);
            if (t.isSqr())
                break;

            datacopy[0]++;
        }
        y.set(t).sqrt();
        if (y.sign() < 0)
            y.negate();

        if (field.cofac != null)
            mul(field.cofac);

        return this;*/
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public int sign() {
        if (infFlag != 0)
            return 0;

        return y.sign();
        /*
        point_ptr p = e->data;
        if (p->inf_flag) return 0;
        return element_sign(p->y);
        */
    }

    public String toString() {
        return String.format("{x=%s,y=%s,infFlag=%d}", x, y, infFlag);
    }


    public boolean isValid() {
        Element t0, t1;
        int result;

        if (infFlag != 0)
            return true;

        t0 = field.getTargetField().newElement();
        t1 = field.getTargetField().newElement();
        t0.set(x).square().add(getField().getA()).mul(x).add(getField().getB());
        t1.set(y).square();

        return t0.compareTo(t1) == 0;
    }


    protected void twiceInternal() {
        //lambda = (3x^2 + a) / 2y
        Element lambda = x.duplicate().square().mul(3).add(getField().a).mul(y.duplicate().twice().invert());

        // x1 = lambda^2 - 2x
        Element x1 = lambda.duplicate().square().sub(x.duplicate().twice());

        // y1 = (x - x1)lambda - y
        Element y1 = x.duplicate().sub(x1).mul(lambda).sub(y);

        x.set(x1);
        y.set(y1);

        infFlag = 0;

        /*
        element_t lambda, e0, e1;
        field_ptr f = r->x->field;

        element_init(lambda, f);
        element_init(e0, f);
        element_init(e1, f);

        //lambda = (3x^2 + a) / 2y
        element_square(lambda, p->x);
        element_mul_si(lambda, lambda, 3);
        element_add(lambda, lambda, a);

        element_double(e0, p->y);

        element_invert(e0, e0);
        element_mul(lambda, lambda, e0);
        //x1 = lambda^2 - 2x
        //element_add(e1, p->x, p->x);
        element_double(e1, p->x);
        element_square(e0, lambda);
        element_sub(e0, e0, e1);
        //y1 = (x - x1)lambda - y
        element_sub(e1, p->x, e0);
        element_mul(e1, e1, lambda);
        element_sub(e1, e1, p->y);

        element_set(r->x, e0);
        element_set(r->y, e1);
        r->inf_flag = 0;

        element_clear(lambda);
        element_clear(e0);
        element_clear(e1);
        */
    }


}
