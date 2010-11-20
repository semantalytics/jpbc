package it.unisa.dia.gas.crypto.jpbc.signature.bls.params;

import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class BLSPrivateKeyParameters extends BLSKeyParameters {
    private Element sk;


    public BLSPrivateKeyParameters(BLSParameters parameters, Element sk) {
        super(true, parameters);
        this.sk = sk;
    }


    public Element getSk() {
        return sk;
    }
}