package it.unisa.dia.gas.crypto.jpbc.rfid.utma.weak.params;

import org.bouncycastle.crypto.CipherParameters;

import java.io.Serializable;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMAWeakParameters implements CipherParameters, Serializable {
    private UTMAWeakPublicParameters publicParameters;
    private UTMAWeakMasterSecretKeyParameters masterSecretKeyParameters;


    public UTMAWeakParameters(UTMAWeakPublicParameters publicParameters, UTMAWeakMasterSecretKeyParameters masterSecretKeyParameters) {
        this.publicParameters = publicParameters;
        this.masterSecretKeyParameters = masterSecretKeyParameters;
    }


    public UTMAWeakPublicParameters getPublicParameters() {
        return publicParameters;
    }

    public UTMAWeakMasterSecretKeyParameters getMasterSecretKeyParameters() {
        return masterSecretKeyParameters;
    }
}

