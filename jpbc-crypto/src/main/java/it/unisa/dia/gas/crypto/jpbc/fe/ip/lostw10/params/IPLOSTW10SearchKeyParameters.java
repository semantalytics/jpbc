package it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params;

import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class IPLOSTW10SearchKeyParameters extends IPLOSTW10KeyParameters {
    private Element K;

    public IPLOSTW10SearchKeyParameters(IPLOSTW10Parameters parameters, Element k) {
        super(true, parameters);
        K = k;
    }

    public Element getK() {
        return K;
    }
}