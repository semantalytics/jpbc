package it.unisa.dia.gas.jpbc.hve;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

import java.util.List;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
*/
public class Pk {

    protected Pairing pairing;
    protected Element g;
    protected Element Y;
    protected List<Element> T,V,R,M;


    public Pk(Pairing pairing, Element g, Element y, List<Element> t, List<Element> v, List<Element> r, List<Element> m) {
        this.pairing = pairing;
        this.g = g;
        Y = y;
        T = t;
        V = v;
        R = r;
        M = m;
    }

}
