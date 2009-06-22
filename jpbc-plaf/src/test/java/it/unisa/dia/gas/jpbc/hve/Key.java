package it.unisa.dia.gas.jpbc.hve;

import it.unisa.dia.gas.jpbc.Element;

import java.util.List;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class Key {
    List<Element> Y, L;

    public Key(List<Element> y, List<Element> l) {
        Y = y;
        L = l;
    }
}
