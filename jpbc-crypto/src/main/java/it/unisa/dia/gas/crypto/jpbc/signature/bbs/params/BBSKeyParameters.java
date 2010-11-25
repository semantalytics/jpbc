package it.unisa.dia.gas.crypto.jpbc.signature.bbs.params;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

import java.io.Serializable;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class BBSKeyParameters extends AsymmetricKeyParameter implements Serializable {
    private BBSParameters parameters;


    public BBSKeyParameters(boolean isPrivate, BBSParameters parameters) {
        super(isPrivate);
        this.parameters = parameters;
    }


    public BBSParameters getParameters() {
        return parameters;
    }

}
