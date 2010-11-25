package it.unisa.dia.gas.crypto.jpbc.signature.bbs.params;

import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class BBSPrivateKeyParameters extends BBSKeyParameters {
    private Element sk;


    public BBSPrivateKeyParameters(BBSParameters parameters, Element sk) {
        super(true, parameters);
        this.sk = sk;
    }


    public Element getSk() {
        return sk;
    }
}