package it.unisa.dia.gas.plaf.jpbc.field.curve;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.FieldOver;
import it.unisa.dia.gas.plaf.jpbc.field.generic.GenericPointElement;
import it.unisa.dia.gas.plaf.jpbc.util.BigIntegerUtils;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class CurveElement<E extends Element> extends GenericPointElement<E> {
    protected int infFlag;
    protected SecureRandom random;
    protected CurveField curveField;


    public CurveElement(FieldOver field) {
        super(field);
        this.curveField = (CurveField) field;

        this.x = (E) field.getTargetField().newElement();
        this.y = (E) field.getTargetField().newElement();
        this.infFlag = 1;
    }

    public CurveElement(CurveElement curveElement) {
        super(curveElement.field);
        this.curveField = curveElement.getField();

        this.x = (E) curveElement.x.duplicate();
        this.y = (E) curveElement.y.duplicate();
        this.infFlag = curveElement.infFlag;
    }


    public CurveField getField() {
        return (CurveField) field;
    }

    public Element getImmutable() {
        return new ImmutableCurveElement<E>(this);
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
        // Apply the Chord-TAngent Law of Composition
        // Consider P1 = this = (x1, y1);
        //          P2 = e = (x2, y2);

        if (infFlag != 0) {
            set(e);
            return this;
        }

        CurveElement element = (CurveElement) e;

        if (element.infFlag != 0)
            return this;

        if (x.isEqual(element.x)) {
            if (y.isEqual(element.y)) {
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
            // P1 != P2, so the slope of the line Lthrough P1 and P2 is
            // lambda = (y2-y1)/(x2-x1)
            Element lambda = element.y.duplicate().sub(y).mul(element.x.duplicate().sub(x).invert());

            // x3 = lambda^2 - x1 - x2
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
        return BigIntegerUtils.isOdd(field.getOrder()) || duplicate().pow(field.getOrder().subtract(BigInteger.ONE).divide(BigIntegerUtils.TWO)).isOne();
    }

    public boolean isEqual(Element e) {
        if (this == e)
            return true;

        CurveElement element = (CurveElement) e;

        return infFlag == 0 && x.isEqual(element.x) && y.isEqual(element.y);

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

        infFlag = 0;
        x.setFromHash(source, offset, length);

        Element t = field.getTargetField().newElement();
        for(;;) {
            t.set(x).square().add(curveField.a).mul(x).add(curveField.b);
            if (t.isSqr())
                break;

            x.square().add(t.setToOne());
        }
        y.set(t).sqrt();
        if (y.sign() < 0)
            y.negate();

        if (curveField.cofac != null)
            mul(curveField.cofac);

        return this;
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


    public int getLengthInBytesCompressed() {
        return x.getLengthInBytes() + 1;
    }

    public byte[] toBytesCompressed() {
        byte[] xBytes = x.toBytes();
        byte[] result = new byte[getLengthInBytesCompressed()];
        System.arraycopy(xBytes, 0, result, 0, xBytes.length);

        if (y.sign() > 0)
            result[xBytes.length] = 1;
        else
            result[xBytes.length] = 0;

        return result;
    }

    public int setFromBytesCompressed(byte[] source) {
        return setFromBytesCompressed(source, 0);
    }

    public int setFromBytesCompressed(byte[] source, int offset) {
        int len = x.setFromBytes(source, offset);
        setPointFromX();

        if (source[offset + len] == 1) {
            if (y.sign() < 0)
                y.negate();
        } else if (y.sign() > 0)
            y.negate();
        return len + 1;
    }

    public int getLengthInBytesX() {
        return x.getLengthInBytes();
    }

    public byte[] toBytesX() {
        return x.toBytes();
    }

    public int setFromBytesX(byte[] source) {
        return setFromBytesX(source, 0);
    }

    public int setFromBytesX(byte[] source, int offset) {
        int len = x.setFromBytes(source, offset);
        setPointFromX();
        return len;
    }


    public boolean isValid() {
        Element t0, t1;

        if (infFlag != 0)
            return true;

        t0 = field.getTargetField().newElement();
        t1 = field.getTargetField().newElement();
        t0.set(x).square().add(getField().getA()).mul(x).add(getField().getB());
        t1.set(y).square();

        return t0.isEqual(t1);
    }


    protected void twiceInternal() {
        // We have P1 = P2 so the tangent line T at P1 ha slope
        //lambda = (3x^2 + a) / 2y
        Element lambda = x.duplicate().square().mul(3).add(getField().a).mul(y.duplicate().twice().invert());

        // x3 = lambda^2 - 2x
        Element x3 = lambda.duplicate().square().sub(x.duplicate().twice());

        // y3 = (x - x3) lambda - y
        Element y3 = x.duplicate().sub(x3).mul(lambda).sub(y);

        x.set(x3);
        y.set(y3);
        infFlag = 0;
    }

    protected void setPointFromX() {
        infFlag = 0;
        y.set(x.duplicate().square().add(curveField.getA()).mul(x).add(curveField.getB()).sqrt());
    }

}
