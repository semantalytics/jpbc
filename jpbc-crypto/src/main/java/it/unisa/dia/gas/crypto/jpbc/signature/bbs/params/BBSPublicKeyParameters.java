package it.unisa.dia.gas.crypto.jpbc.signature.bbs.params;

import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class BBSPublicKeyParameters extends BBSKeyParameters {
    private Element pk;


    public BBSPublicKeyParameters(BBSParameters parameters, Element pk) {
        super(false, parameters);
        this.pk = pk;
    }


    public Element getPk() {
        return pk;
    }
}
