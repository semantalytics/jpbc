package it.unisa.dia.gas.crypto.jpbc.encryption.utma.bdp10.params;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMABDP10StrongRandomizeParameters extends UTMABDP10StrongKeyParameters {
    private UTMABDP10StrongRPublicParameters rPublicParameters;


    public UTMABDP10StrongRandomizeParameters(UTMABDP10StrongPublicParameters publicParameters,
                                              UTMABDP10StrongRPublicParameters rPublicParameters) {
        super(true, publicParameters);
        this.rPublicParameters = rPublicParameters;
    }


    public UTMABDP10StrongRPublicParameters getRPublicParameters() {
        return rPublicParameters;
    }


}
