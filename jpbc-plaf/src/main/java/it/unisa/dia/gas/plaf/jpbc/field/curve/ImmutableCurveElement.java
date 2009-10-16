package it.unisa.dia.gas.plaf.jpbc.field.curve;

import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class ImmutableCurveElement<E extends Element> extends CurveElement<E> {

    public ImmutableCurveElement(CurveElement curveElement) {
        super(curveElement);
        
        this.immutable = true;
    }

    @Override
    public CurveElement powZn(Element e) {
        return duplicate().powZn(e);
    }

    @Override
    public CurveElement mul(Element e) {
        return duplicate().mul(e);
    }
}
