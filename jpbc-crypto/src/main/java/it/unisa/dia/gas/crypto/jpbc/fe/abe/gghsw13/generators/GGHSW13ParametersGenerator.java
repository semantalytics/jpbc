package it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.generators;

import it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.params.GGHSW13Parameters;
import it.unisa.dia.gas.jpbc.Pairing;


/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class GGHSW13ParametersGenerator {

    public static GGHSW13Parameters generate(final Pairing pairing, final int n) {
        return new GGHSW13Parameters(pairing, n);
    }
}