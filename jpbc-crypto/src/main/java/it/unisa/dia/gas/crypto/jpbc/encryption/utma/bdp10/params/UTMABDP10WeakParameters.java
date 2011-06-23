package it.unisa.dia.gas.crypto.jpbc.encryption.utma.bdp10.params;

import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMABDP10WeakParameters implements CipherParameters {
    private UTMABDP10WeakPublicParameters publicParameters;
    private UTMABDP10WeakMasterSecretKeyParameters masterSecretKeyParameters;


    public UTMABDP10WeakParameters(UTMABDP10WeakPublicParameters publicParameters, UTMABDP10WeakMasterSecretKeyParameters masterSecretKeyParameters) {
        this.publicParameters = publicParameters;
        this.masterSecretKeyParameters = masterSecretKeyParameters;
    }


    public UTMABDP10WeakPublicParameters getPublicParameters() {
        return publicParameters;
    }

    public UTMABDP10WeakMasterSecretKeyParameters getMasterSecretKeyParameters() {
        return masterSecretKeyParameters;
    }
}

