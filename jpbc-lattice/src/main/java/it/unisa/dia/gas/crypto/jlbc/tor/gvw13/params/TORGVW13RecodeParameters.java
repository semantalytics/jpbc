package it.unisa.dia.gas.crypto.jlbc.tor.gvw13.params;

import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class TORGVW13RecodeParameters extends TORGVW13KeyParameters {

    private Element R;

    public TORGVW13RecodeParameters(TORGVW13Parameters parameters, Element R) {
        super(true, parameters);

        this.R = R;
    }

    public Element getR() {
        return R;
    }
}
