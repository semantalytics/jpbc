package it.unisa.dia.gas.plaf.jpbc.pbc;

import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class ImmutablePBCElement extends PBCElement {

    public ImmutablePBCElement(PBCElement pbcElement) {
        super(pbcElement);
        this.immutable = true;
    }
    
    
    @Override
    public PBCElement powZn(Element e) {
        return duplicate().powZn(e);
    }

    @Override
    public PBCElement mul(Element e) {
        return duplicate().mul(e);
    }
    
}
