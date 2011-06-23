package it.unisa.dia.gas.crypto.jpbc.encryption.utma.bdp10.params;

import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMAStrongPublicKeyParameters extends UTMAStrongKeyParameters {
    private Element pk;


    public UTMAStrongPublicKeyParameters(UTMAStrongPublicParameters publicParameters, Element pk) {
        super(false, publicParameters);
        
        this.pk = pk;
    }


    public Element getPk() {
        return pk;
    }

}