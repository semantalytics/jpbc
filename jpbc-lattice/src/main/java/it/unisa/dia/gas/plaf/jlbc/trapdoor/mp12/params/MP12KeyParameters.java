package it.unisa.dia.gas.plaf.jlbc.trapdoor.mp12.params;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class MP12KeyParameters extends AsymmetricKeyParameter {

    private MP12Parameters parameters;


    public MP12KeyParameters(boolean isPrivate, MP12Parameters parameters) {
        super(isPrivate);
        this.parameters = parameters;
    }


    public MP12Parameters getParameters() {
        return parameters;
    }

}


