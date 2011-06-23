package it.unisa.dia.gas.crypto.jpbc.encryption.utma.bdp10.params;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMAStrongRandomizeParameters extends UTMAStrongKeyParameters {
    private UTMAStrongRPublicParameters rPublicParameters;


    public UTMAStrongRandomizeParameters(UTMAStrongPublicParameters publicParameters,
                                         UTMAStrongRPublicParameters rPublicParameters) {
        super(true, publicParameters);
        this.rPublicParameters = rPublicParameters;
    }


    public UTMAStrongRPublicParameters getRPublicParameters() {
        return rPublicParameters;
    }


}
