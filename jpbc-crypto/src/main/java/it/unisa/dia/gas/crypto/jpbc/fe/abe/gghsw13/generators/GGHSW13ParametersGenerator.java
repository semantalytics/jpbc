package it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.generators;

import it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.params.GGHSW13Parameters;
import it.unisa.dia.gas.jpbc.Pairing;

import java.security.SecureRandom;


/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class GGHSW13ParametersGenerator {

    private SecureRandom random;
    private Pairing pairing;
    private int n;


    public GGHSW13ParametersGenerator init(SecureRandom random, Pairing pairing, int n) {
        this.random = random;
        this.pairing = pairing;
        this.n = n;

        return this;
    }


    public GGHSW13Parameters generateParameters() {
        return new GGHSW13Parameters(pairing, n);
    }

}