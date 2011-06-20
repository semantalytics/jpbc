package it.unisa.dia.gas.crypto.jpbc.rfid.utma.strong.params;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMAStrongKeyParameters extends AsymmetricKeyParameter {
    private UTMAStrongPublicParameters publicParameters;


    public UTMAStrongKeyParameters(boolean isPrivate, UTMAStrongPublicParameters publicParameters) {
        super(isPrivate);
        this.publicParameters = publicParameters;
    }


    public UTMAStrongPublicParameters getParameters() {
        return publicParameters;
    }

}