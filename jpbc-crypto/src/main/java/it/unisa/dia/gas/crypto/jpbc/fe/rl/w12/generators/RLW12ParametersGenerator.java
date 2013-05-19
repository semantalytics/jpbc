package it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.generators;

import it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.params.DFAAlphabet;
import it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.params.RLW12Parameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;


/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class RLW12ParametersGenerator {
    private PairingParameters parameters;
    private DFAAlphabet alphabet;

    private Pairing pairing;


    public RLW12ParametersGenerator init(PairingParameters parameters, DFAAlphabet alphabet) {
        this.parameters = parameters;
        this.alphabet = alphabet;
        this.pairing = PairingFactory.getPairing(this.parameters);

        return this;
    }


    public RLW12Parameters generateParameters() {
        Element g = pairing.getG1().newElement().setToRandom();

        return new RLW12Parameters(parameters, g.getImmutable(), alphabet);
    }

}