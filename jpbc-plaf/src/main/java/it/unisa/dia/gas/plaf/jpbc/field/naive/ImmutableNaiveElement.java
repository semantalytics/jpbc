package it.unisa.dia.gas.plaf.jpbc.field.naive;

import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class ImmutableNaiveElement extends NaiveElement {

    public ImmutableNaiveElement(NaiveElement naiveElement) {
        super(naiveElement);
        this.immutable = true;
    }

    @Override
    public NaiveElement powZn(Element e) {
        return duplicate().powZn(e);
    }

    @Override
    public NaiveElement mul(Element e) {
        return duplicate().mul(e);
    }

}
