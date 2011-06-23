package it.unisa.dia.gas.crypto.jpbc.encryption.utma.bdp10.params;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMABDP10StrongKeyParameters extends AsymmetricKeyParameter {
    private UTMABDP10StrongPublicParameters publicParameters;


    public UTMABDP10StrongKeyParameters(boolean isPrivate, UTMABDP10StrongPublicParameters publicParameters) {
        super(isPrivate);
        this.publicParameters = publicParameters;
    }


    public UTMABDP10StrongPublicParameters getParameters() {
        return publicParameters;
    }

}