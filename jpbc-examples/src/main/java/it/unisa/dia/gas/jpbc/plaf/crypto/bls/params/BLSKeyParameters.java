package it.unisa.dia.gas.jpbc.plaf.crypto.bls.params;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

import java.io.Serializable;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class BLSKeyParameters extends AsymmetricKeyParameter implements Serializable {
    private BLSParameters parameters;


    public BLSKeyParameters(boolean isPrivate, BLSParameters parameters) {
        super(isPrivate);
        this.parameters = parameters;
    }


    public BLSParameters getParameters() {
        return parameters;
    }

}
