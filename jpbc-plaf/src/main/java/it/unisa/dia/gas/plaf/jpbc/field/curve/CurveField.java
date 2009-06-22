package it.unisa.dia.gas.plaf.jpbc.field.curve;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.field.generic.GenericFieldOver;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class CurveField<F extends Field> extends GenericFieldOver<F, CurveElement> {
    protected Element a, b;
    protected CurveElement gen, genNoCofac;
    protected BigInteger order, cofac;


    public CurveField(Element a, Element b, BigInteger order, BigInteger cofac) {
        super((F) a.getField());

        this.a = a;
        this.b = b;
        this.order = order;
        this.cofac = cofac;

        initGen();
    }


    public CurveElement newElement() {
        return new CurveElement(this);
    }

    public BigInteger getOrder() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public CurveElement getNqr() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public int getFixedLengthInBytes() {
        throw new IllegalStateException("Not implemented yet!!!");
    }


    public Element getA() {
        return a;
    }

    public Element getB() {
        return b;
    }

    public CurveField twist() {
//        System.out.println("twist");
        Element nqr = getTargetField().getNqr();

        a.mul(nqr).mul(nqr);
        b.mul(nqr).mul(nqr).mul(nqr);

        initGen();

        return this;
    }


    protected void initGen() {
        genNoCofac = getCurveRandomNoCofacSolvefory();
        if (cofac != null) {
            gen = genNoCofac.duplicate().mul(cofac);
        } else {
            gen = genNoCofac.duplicate();
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