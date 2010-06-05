package it.unisa.dia.gas.plaf.jpbc.field.curve;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.field.generic.GenericFieldOver;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class CurveField<F extends Field> extends GenericFieldOver<F, CurveElement> {

    public static <F extends Field> CurveField<F> newCurveFieldJ(Element j, BigInteger order, BigInteger cofac) {
        // Assumes j != 0, 1728

        Element a, b;

        a = j.getField().newElement();
        b = j.getField().newElement();

        a.set(1728).sub(j).invert().mul(j);

        //b = 2 j / (1728 - j)
        b.set(a).add(a);
        //a = 3 j / (1728 - j)
        a.add(b);

        return new CurveField<F>(a, b, order, cofac);
    }

    protected Element a, b;
    protected CurveElement gen, genNoCofac;
    protected BigInteger order, cofac;

    // A non-NULL quotient_cmp means we are working with the quotient group of
    // order #E / quotient_cmp, and the points are actually coset
    // representatives. Thus for a comparison, we must multiply by quotient_cmp
    // before comparing.
    protected BigInteger quotient_cmp = null;



    public CurveField(Element a, Element b, BigInteger order, BigInteger cofac) {
        super((F) a.getField());

        this.a = a;
        this.b = b;
        this.order = order;
        this.cofac = cofac;

        initGen();
    }


    public CurveField(Element a, Element b, BigInteger order, BigInteger cofac, BigInteger genNoCofac) {
        super((F) a.getField());

        this.a = a;
        this.b = b;
        this.order = order;
        this.cofac = cofac;

        initGen(genNoCofac);
    }

    public CurveField(Element b, BigInteger order, BigInteger cofac) {
        this(b.getField().newZeroElement(), b, order, cofac);
    }


    public CurveElement newElement() {
        return new CurveElement(this);
    }

    public BigInteger getOrder() {
        return order;
    }

    public CurveElement getNqr() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public int getLengthInBytes() {
        return getTargetField().getLengthInBytes() * 2;
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CurveField)) return false;

        CurveField that = (CurveField) o;

        if (!a.equals(that.a)) return false;
        if (!b.equals(that.b)) return false;
        if (cofac != null ? !cofac.equals(that.cofac) : that.cofac != null) return false;
        if (!order.equals(that.order)) return false;

        return true;
    }

    public int hashCode() {
        int result = a.hashCode();
        result = 31 * result + b.hashCode();
        result = 31 * result + order.hashCode();
        result = 31 * result + (cofac != null ? cofac.hashCode() : 0);
        return result;
    }


    public Element getA() {
        return a;
    }

    public Element getB() {
        return b;
    }

    public BigInteger getQuotient_cmp() {
        return quotient_cmp;
    }

    public void setQuotient_cmp(BigInteger quotient_cmp) {
        this.quotient_cmp = quotient_cmp;
    }

    /**
     * Existing points are invalidated as this mangles c.
     * Consider the curve E′ given by Y^2 = X^3 + a v^2 X + v^3 b, which
     * we call the (quadratic) twist of the curve E, where
     * v is a quadratic nonresidue in Fq
     *
     * @return the twisted curve.
     */
    public CurveField twist() {
        Element nqr = getTargetField().getNqr();

        a.mul(nqr).mul(nqr);
        b.mul(nqr).mul(nqr).mul(nqr);

        initGen();

        return this;
    }

    public CurveElement getGenNoCofac() {
        return genNoCofac;
    }


    protected void initGen() {
        genNoCofac = getCurveRandomNoCofacSolvefory();
        if (cofac != null) {
            gen = genNoCofac.duplicate().mul(cofac);
        } else {
            gen = genNoCofac.duplicate();
        }
    }

    protected void initGen(BigInteger genNoCofac) {
        if (genNoCofac == null) {
            this.genNoCofac = getCurveRandomNoCofacSolvefory();
        } else {
            CurveElement element = new CurveElement(this);
            element.setFromBytes(genNoCofac.toByteArray());
            this.genNoCofac = element;
        }

        if (cofac != null) {
            gen = this.genNoCofac.duplicate().mul(cofac);
        } else {
            gen = this.genNoCofac.duplicate();
        }
    }

    protected CurveElement getCurveRandomNoCofacSolvefory() {
        //TODO: with 0.5 probability negate y-coord

        Element t = targetField.newElement();

        CurveElement element = new CurveElement(this);
        element.infFlag = 0;

        do {
            t.set(element.getX().setToRandom()).square().add(a).mul(element.getX()).add(b);
        } while (!t.isSqr());
        element.getY().set(t.sqrt());

        return element;
    }

}