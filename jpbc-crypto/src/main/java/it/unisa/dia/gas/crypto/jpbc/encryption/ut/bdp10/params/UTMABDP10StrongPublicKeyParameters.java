package it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.params;

import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMABDP10StrongPublicKeyParameters extends UTMABDP10StrongKeyParameters {
    private Element pk;


    public UTMABDP10StrongPublicKeyParameters(UTMABDP10StrongPublicParameters publicParameters, Element pk) {
        super(false, publicParameters);
        
        this.pk = pk;
    }


    public Element getPk() {
        return pk;
    }

}