package it.unisa.dia.gas.crypto.jpbc.signature.ps06.params;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

import java.io.Serializable;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PS06KeyParameters extends AsymmetricKeyParameter implements Serializable {
    private PS06Parameters parameters;


    public PS06KeyParameters(boolean isPrivate, PS06Parameters parameters) {
        super(isPrivate);
        this.parameters = parameters;
    }


    public PS06Parameters getParameters() {
        return parameters;
    }

}
