package it.unisa.dia.gas.crypto.jpbc.tor.gvw13.params;

import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class TORGVW13SecretKeyParameters extends TORGVW13KeyParameters {

    private Element t;


    public TORGVW13SecretKeyParameters(TORGVW13Parameters parameters, Element t) {
        super(true, parameters);

        this.t = t.getImmutable();
    }


    public Element getT() {
        return t;
    }
}
