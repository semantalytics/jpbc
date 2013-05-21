package it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.generators;

import it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.params.GGHSW13Parameters;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.engine.CTL13MMInstance;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.generators.CTL13MMInstanceGenerator;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.pairing.CTL13MMPairing;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters.CTL13InstanceParameters;

import java.security.SecureRandom;


/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class GGHSW13ParametersGenerator {

    private SecureRandom random;
    private CTL13InstanceParameters parameters;
    private int n;

    private Pairing pairing;


    public GGHSW13ParametersGenerator(SecureRandom random, CTL13InstanceParameters parameters, int n) {
        this.random = random;
        this.parameters = parameters;
        this.n = n;
    }

    public GGHSW13ParametersGenerator init() {
        CTL13MMInstance instance = new CTL13MMInstanceGenerator(random, parameters).generateInstance();
        this.pairing = new CTL13MMPairing(random, instance);

        return this;
    }

    public GGHSW13Parameters generateParameters() {
        return new GGHSW13Parameters(pairing, n);
    }

}