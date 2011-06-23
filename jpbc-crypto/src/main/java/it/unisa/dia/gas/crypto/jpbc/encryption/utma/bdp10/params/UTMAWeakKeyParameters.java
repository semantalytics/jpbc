package it.unisa.dia.gas.crypto.jpbc.encryption.utma.bdp10.params;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMAWeakKeyParameters extends AsymmetricKeyParameter {
    private UTMAWeakPublicParameters publicParameters;


    public UTMAWeakKeyParameters(boolean isPrivate, UTMAWeakPublicParameters publicParameters) {
        super(isPrivate);
        this.publicParameters = publicParameters;
    }


    public UTMAWeakPublicParameters getParameters() {
        return publicParameters;
    }


}