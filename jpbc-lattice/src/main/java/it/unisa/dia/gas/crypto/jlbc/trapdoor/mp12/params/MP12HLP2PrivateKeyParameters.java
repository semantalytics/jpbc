package it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.params;

import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class MP12HLP2PrivateKeyParameters extends MP12KeyParameters {

    protected Element R;


    public MP12HLP2PrivateKeyParameters(MP12Parameters parameters, Element R) {
        super(true, parameters);
        this.R = R;
    }

    public Element getR() {
        return R;
    }
}
