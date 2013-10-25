package it.unisa.dia.gas.crypto.jpbc.fe.abe.gghvv13.generators;

import it.unisa.dia.gas.crypto.jpbc.fe.abe.gghvv13.params.GGHVV13Parameters;
import it.unisa.dia.gas.jpbc.Pairing;

import java.security.SecureRandom;


/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class GGHVV13ParametersGenerator {

    private SecureRandom random;
    private Pairing pairing;
    private int n;


    public GGHVV13ParametersGenerator init(SecureRandom random, Pairing pairing, int n) {
        this.random = random;
        this.pairing = pairing;
        this.n = n;

        return this;
    }


    public GGHVV13Parameters generateParameters() {
        return new GGHVV13Parameters(pairing, n);
    }

}