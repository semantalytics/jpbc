package it.unisa.dia.gas.crypto.jpbc.encryption.utma.bdp10.params;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMABDP10WeakKeyParameters extends AsymmetricKeyParameter {
    private UTMABDP10WeakPublicParameters publicParameters;


    public UTMABDP10WeakKeyParameters(boolean isPrivate, UTMABDP10WeakPublicParameters publicParameters) {
        super(isPrivate);
        this.publicParameters = publicParameters;
    }


    public UTMABDP10WeakPublicParameters getParameters() {
        return publicParameters;
    }


}