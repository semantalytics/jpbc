package it.unisa.dia.gas.crypto.jpbc.signature.bbs.generators;

import it.unisa.dia.gas.crypto.jpbc.signature.bbs.params.BBSParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class BBSParametersGenerator {
    private CurveParams curveParams;
    private Pairing pairing;


    public void init(CurveParams curveParams) {
        this.curveParams = curveParams;
        this.pairing = PairingFactory.getPairing(curveParams);
    }

    public BBSParameters generateParameters() {
        Element g = pairing.getG2().newRandomElement();

        return new BBSParameters(curveParams, g.getImmutable());
    }
}
