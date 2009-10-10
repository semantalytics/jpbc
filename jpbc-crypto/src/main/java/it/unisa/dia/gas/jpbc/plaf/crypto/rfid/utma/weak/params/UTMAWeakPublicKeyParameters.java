package it.unisa.dia.gas.jpbc.plaf.crypto.rfid.utma.weak.params;

import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMAWeakPublicKeyParameters extends UTMAWeakKeyParameters {
    private Element pk;

    public UTMAWeakPublicKeyParameters(UTMAWeakPublicParameters publicParameters, Element pk) {
        super(false, publicParameters);
        this.pk = pk;
    }

    public Element getPk() {
        return pk;
    }
}
