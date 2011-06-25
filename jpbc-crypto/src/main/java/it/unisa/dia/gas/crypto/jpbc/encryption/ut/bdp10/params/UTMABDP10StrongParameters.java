package it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.params;

import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMABDP10StrongParameters implements CipherParameters {
    private UTMABDP10StrongPublicParameters publicParameters;
    private UTMABDP10StrongRPublicParameters rPublicParameters;

    private UTMABDP10StrongMasterSecretKeyParameters masterSecretKeyParameters;


    public UTMABDP10StrongParameters(UTMABDP10StrongPublicParameters publicParameters,
                                     UTMABDP10StrongRPublicParameters rPublicParameters,
                                     UTMABDP10StrongMasterSecretKeyParameters masterSecretKeyParameters) {
        this.publicParameters = publicParameters;
        this.rPublicParameters = rPublicParameters;

        this.masterSecretKeyParameters = masterSecretKeyParameters;
    }


    public UTMABDP10StrongPublicParameters getPublicParameters() {
        return publicParameters;
    }

    public UTMABDP10StrongRPublicParameters getRPublicParameters() {
        return rPublicParameters;
    }

    public UTMABDP10StrongMasterSecretKeyParameters getMasterSecretKeyParameters() {
        return masterSecretKeyParameters;
    }


}
