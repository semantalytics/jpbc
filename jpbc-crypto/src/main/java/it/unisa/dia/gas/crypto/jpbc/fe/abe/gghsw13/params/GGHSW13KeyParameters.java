package it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.params;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class GGHSW13KeyParameters extends AsymmetricKeyParameter {

    private final GGHSW13Parameters parameters;

    public GGHSW13KeyParameters(final boolean isPrivate, final GGHSW13Parameters parameters) {
        super(isPrivate);
        this.parameters = parameters;
    }

    public GGHSW13Parameters getParameters() {
        return parameters;
    }
}


