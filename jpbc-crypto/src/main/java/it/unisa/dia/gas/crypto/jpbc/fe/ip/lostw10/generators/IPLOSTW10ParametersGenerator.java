package it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.generators;

import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params.IPLOSTW10Parameters;
import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;


/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class IPLOSTW10ParametersGenerator {
    private CurveParameters curveParams;
    private int n;

    private Pairing pairing;


    public IPLOSTW10ParametersGenerator init(CurveParams curveParams, int n) {
        this.curveParams = curveParams;
        this.n = n;
        this.pairing = PairingFactory.getPairing(curveParams);

        return this;
    }


    public IPLOSTW10Parameters generateParameters() {
        Element g = pairing.getG1().newElement().setToRandom();

        return new IPLOSTW10Parameters(curveParams, g.getImmutable(), n);
    }

}