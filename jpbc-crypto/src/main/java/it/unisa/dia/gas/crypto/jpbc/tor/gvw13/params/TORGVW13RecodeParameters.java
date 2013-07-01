package it.unisa.dia.gas.crypto.jpbc.tor.gvw13.params;

import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TORGVW13RecodeParameters extends TORGVW13KeyParameters {

    private Element rk;

    public TORGVW13RecodeParameters(TORGVW13Parameters parameters, Element rk) {
        super(true, parameters);
        this.rk = rk;
    }

    public Element getRk() {
        return rk;
    }
}
