package it.unisa.dia.gas.jpbc.hve;

import it.unisa.dia.gas.jpbc.Element;

import java.util.List;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
*/
public class Msk {
    protected Element y;
    protected List<Element> t,v,r,m;

    public Msk(Element y, List<Element> t, List<Element> v, List<Element> r, List<Element> m) {
        this.y = y;
        
        this.t = t;
        this.v = v;
        this.r = r;
        this.m = m;
    }
}
