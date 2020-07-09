package it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.params;

import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class GGHSW13MasterSecretKeyParameters extends GGHSW13KeyParameters {

    private final Element alpha;


    public GGHSW13MasterSecretKeyParameters(final GGHSW13Parameters parameters, final Element alpha) {
        super(true, parameters);
        this.alpha = alpha.getImmutable();
    }

    public Element getAlpha() {
        return alpha;
    }
}
