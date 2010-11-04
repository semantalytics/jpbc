package it.unisa.dia.gas.crypto.jpbc.bls.generators;

import it.unisa.dia.gas.crypto.jpbc.bls.params.BLSParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class BLSParametersGenerator {
    private CurveParams curveParams;
    private Pairing pairing;


    public void init(CurveParams curveParams) {
        this.curveParams = curveParams;
        this.pairing = PairingFactory.getPairing(curveParams);
    }

    public BLSParameters generateParameters() {
        Element g = pairing.getG2().newRandomElement();

        return new BLSParameters(curveParams, g.getImmutable());
    }
}
