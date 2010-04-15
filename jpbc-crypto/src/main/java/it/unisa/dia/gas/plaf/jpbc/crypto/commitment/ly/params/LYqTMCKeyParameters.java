package it.unisa.dia.gas.plaf.jpbc.crypto.commitment.ly.params;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

import java.io.Serializable;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class LYqTMCKeyParameters extends AsymmetricKeyParameter implements Serializable {
    private LYqTMCParameters parameters;


    public LYqTMCKeyParameters(boolean isPrivate, LYqTMCParameters parameters) {
        super(isPrivate);
        this.parameters = parameters;
    }


    public LYqTMCParameters getParameters() {
        return parameters;
    }

}