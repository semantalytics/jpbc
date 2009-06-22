package it.unisa.dia.gas.jpbc.hve;

import it.unisa.dia.gas.jpbc.Element;

import java.util.List;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class CypherText {
    protected Element omega, C0;
    protected List<Element> X, W;

    public CypherText(Element omega, Element c0, List<Element> x, List<Element> w) {
        this.omega = omega;
        C0 = c0;
        X = x;
        W = w;
    }
}
