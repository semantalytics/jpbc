package it.unisa.dia.gas.crypto.jpbc.encryption.utma.bdp10.params;

import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMAStrongParameters implements CipherParameters {
    private UTMAStrongPublicParameters publicParameters;
    private UTMAStrongRPublicParameters rPublicParameters;

    private UTMAStrongMasterSecretKeyParameters masterSecretKeyParameters;


    public UTMAStrongParameters(UTMAStrongPublicParameters publicParameters,
                                UTMAStrongRPublicParameters rPublicParameters,
                                UTMAStrongMasterSecretKeyParameters masterSecretKeyParameters) {
        this.publicParameters = publicParameters;
        this.rPublicParameters = rPublicParameters;

        this.masterSecretKeyParameters = masterSecretKeyParameters;
    }


    public UTMAStrongPublicParameters getPublicParameters() {
        return publicParameters;
    }

    public UTMAStrongRPublicParameters getRPublicParameters() {
        return rPublicParameters;
    }

    public UTMAStrongMasterSecretKeyParameters getMasterSecretKeyParameters() {
        return masterSecretKeyParameters;
    }


}
