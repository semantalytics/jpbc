package it.unisa.dia.gas.jpbc.examples.bls.params;

import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class BLSPublicKeyParameters extends BLSKeyParameters {
    private Element pk;


    public BLSPublicKeyParameters(BLSParameters parameters, Element pk) {
        super(false, parameters);
        this.pk = pk;
    }


    public Element getPk() {
        return pk;
    }
}
