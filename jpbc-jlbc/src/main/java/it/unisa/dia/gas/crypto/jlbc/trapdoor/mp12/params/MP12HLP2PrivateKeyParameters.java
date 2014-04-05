package it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.params;

import it.unisa.dia.gas.jpbc.Matrix;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class MP12HLP2PrivateKeyParameters extends MP12KeyParameters {

    protected Matrix R;


    public MP12HLP2PrivateKeyParameters(MP12Parameters parameters, Matrix R) {
        super(true, parameters);
        this.R = R;
    }

    public Matrix getR() {
        return R;
    }
}
