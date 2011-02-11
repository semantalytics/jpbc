package it.unisa.dia.gas.crypto.jpbc.signature.bls01.generators;

import it.unisa.dia.gas.crypto.jpbc.signature.bls01.params.BLS01Parameters;
import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class BLS01ParametersGenerator {
    private CurveParameters curveParams;
    private Pairing pairing;


    public void init(CurveParameters curveParams) {
        this.curveParams = curveParams;
        this.pairing = PairingFactory.getPairing(curveParams);
    }

    public BLS01Parameters generateParameters() {
        Element g = pairing.getG2().newRandomElement();

        return new BLS01Parameters(curveParams, g.getImmutable());
    }
}
