package it.unisa.dia.gas.plaf.jpbc.crypto.commitment.ly.params;

import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class LYqTMCPrivateKeyParameters extends LYqTMCKeyParameters {
    private Element sk;


    public LYqTMCPrivateKeyParameters(LYqTMCParameters parameters, Element sk) {
        super(true, parameters);
        this.sk = sk;
    }


    public Element getSk() {
        return sk;
    }
}