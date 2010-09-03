package it.unisa.dia.gas.plaf.jpbc.crypto.commitment.ly.generators;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.crypto.commitment.ly.params.LYqTMCParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class LYqTMCParametersGenerator {
    private CurveParams curveParams;
    private Pairing pairing;


    public void init(CurveParams curveParams) {
        this.curveParams = curveParams;
        this.pairing = PairingFactory.getPairing(curveParams);
    }

    public LYqTMCParameters generateParameters() {
        Element g = pairing.getG1().newRandomElement();

        return new LYqTMCParameters(curveParams, g.getImmutable());
    }    
    
}
