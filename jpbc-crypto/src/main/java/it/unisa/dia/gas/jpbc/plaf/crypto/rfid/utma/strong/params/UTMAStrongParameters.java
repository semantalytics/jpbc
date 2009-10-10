package it.unisa.dia.gas.jpbc.plaf.crypto.rfid.utma.strong.params;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMAStrongParameters {
    private UTMAStrongPublicParameters publicParameters;
    private UTMAStrongMasterSecretKeyParameters masterSecretKeyParameters;


    public UTMAStrongParameters(UTMAStrongPublicParameters publicParameters, UTMAStrongMasterSecretKeyParameters masterSecretKeyParameters) {
        this.publicParameters = publicParameters;
        this.masterSecretKeyParameters = masterSecretKeyParameters;
    }


    public UTMAStrongPublicParameters getPublicParameters() {
        return publicParameters;
    }

    public UTMAStrongMasterSecretKeyParameters getMasterSecretKeyParamters() {
        return masterSecretKeyParameters;
    }


}
