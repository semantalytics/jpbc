package it.unisa.dia.gas.crypto.jpbc.encryption.utma.bdp10.params;

import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMABDP10WeakPublicKeyParameters extends UTMABDP10WeakKeyParameters {
    private Element pk;

    public UTMABDP10WeakPublicKeyParameters(UTMABDP10WeakPublicParameters publicParameters, Element pk) {
        super(false, publicParameters);
        this.pk = pk;
    }

    public Element getPk() {
        return pk;
    }
}
