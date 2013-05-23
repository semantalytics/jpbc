package it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.generators;

import it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.params.GGHSW13Parameters;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.engine.CTL13MMInstance;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.generators.CTL13MMInstanceGenerator;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.pairing.CTL13MMPairing;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters.CTL13MMInstanceParameters;

import java.security.SecureRandom;


/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class GGHSW13ParametersGenerator {

    private SecureRandom random;
    private CTL13MMInstance instance;
    private CTL13MMInstanceParameters parameters;
    private int n;

    public GGHSW13ParametersGenerator init(SecureRandom random, CTL13MMInstanceParameters parameters, int n) {
        this.random = random;
        this.parameters = parameters;
        this.instance = null;
        this.n = n;

        return this;
    }

    public GGHSW13ParametersGenerator init(SecureRandom random, CTL13MMInstance instance, int n) {
        this.random = random;
        this.parameters = null;
        this.instance = instance;
        this.n = n;

        return this;
    }

    public GGHSW13Parameters generateParameters() {
        if (parameters != null)
            return new GGHSW13Parameters(
                    new CTL13MMPairing(
                            random,
                            new CTL13MMInstanceGenerator(random, parameters).generateInstance()
                    ),
                    n);
        else if (instance != null) {
            return new GGHSW13Parameters(
                    new CTL13MMPairing(random, instance), n
            );
        } else
            throw new IllegalStateException("Generator not initialized!");
    }

}