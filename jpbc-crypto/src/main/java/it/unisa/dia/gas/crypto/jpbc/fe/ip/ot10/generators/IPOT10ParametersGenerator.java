package it.unisa.dia.gas.crypto.jpbc.fe.ip.ot10.generators;

import it.unisa.dia.gas.crypto.jpbc.fe.ip.ot10.params.IPOT10Parameters;
import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;


/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class IPOT10ParametersGenerator {
    private CurveParameters curveParams;
    private int n;

    private Pairing pairing;


    public IPOT10ParametersGenerator init(CurveParams curveParams, int n) {
        this.curveParams = curveParams;
        this.n = n;
        this.pairing = PairingFactory.getPairing(curveParams);

        return this;
    }


    public IPOT10Parameters generateParameters() {
        Element g = pairing.getG1().newElement().setToRandom();

        return new IPOT10Parameters(curveParams, g.getImmutable(), n);
    }

}