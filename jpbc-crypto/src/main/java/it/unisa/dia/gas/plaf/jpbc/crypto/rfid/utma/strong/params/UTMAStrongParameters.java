package it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.strong.params;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMAStrongParameters {
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
