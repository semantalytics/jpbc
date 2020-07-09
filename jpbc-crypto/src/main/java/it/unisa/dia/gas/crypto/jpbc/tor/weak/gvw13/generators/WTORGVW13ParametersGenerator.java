package it.unisa.dia.gas.crypto.jpbc.tor.weak.gvw13.generators;

import it.unisa.dia.gas.crypto.jpbc.tor.weak.gvw13.params.WTORGVW13Parameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;


/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class WTORGVW13ParametersGenerator {

    private PairingParameters parameters;
    private Pairing pairing;


    public WTORGVW13ParametersGenerator init(final PairingParameters parameters) {
        this.parameters = parameters;
        this.pairing = PairingFactory.getPairing(this.parameters);

        return this;
    }


    public WTORGVW13Parameters generateParameters() {
        final Element a = pairing.getZr().newElement().setToRandom();

        final Element g1 = pairing.getG1().newElement().setToRandom().getImmutable();
        final Element g2 = pairing.getG2().newElement().setToRandom().getImmutable();

        final Element g1a = g1.powZn(a).getImmutable();
        final Element g2a = g2.powZn(a).getImmutable();

        return new WTORGVW13Parameters(parameters, g1, g2, g1a, g2a);
    }

}