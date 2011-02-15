package it.unisa.dia.gas.crypto.jpbc.rfid.utma.strong.params;

import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMAStrongRandomizeParameters implements CipherParameters {
    private UTMAStrongPublicParameters publicParameters;
    private UTMAStrongRPublicParameters rPublicParameters;


    public UTMAStrongRandomizeParameters(UTMAStrongPublicParameters publicParameters,
                                         UTMAStrongRPublicParameters rPublicParameters) {
        this.publicParameters = publicParameters;
        this.rPublicParameters = rPublicParameters;
    }


    public UTMAStrongPublicParameters getPublicParameters() {
        return publicParameters;
    }

    public UTMAStrongRPublicParameters getRPublicParameters() {
        return rPublicParameters;
    }


}
