package it.unisa.dia.gas.crypto.jpbc.tor.gvw13.params;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TORGVW13KeyParameters extends AsymmetricKeyParameter {

    private TORGVW13Parameters parameters;


    public TORGVW13KeyParameters(boolean isPrivate, TORGVW13Parameters parameters) {
        super(isPrivate);
        this.parameters = parameters;
    }


    public TORGVW13Parameters getParameters() {
        return parameters;
    }
}


