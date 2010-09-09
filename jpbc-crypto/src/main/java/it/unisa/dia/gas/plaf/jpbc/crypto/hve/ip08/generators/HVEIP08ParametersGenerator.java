package it.unisa.dia.gas.plaf.jpbc.crypto.hve.ip08.generators;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.crypto.hve.ip08.params.HVEIP08Parameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HVEIP08ParametersGenerator {
    private CurveParams curveParams;
    private int[] attributeLengths;

    private Pairing pairing;


    public void init(CurveParams curveParams, int[] attributeLengths) {
        this.curveParams = curveParams;
        this.attributeLengths = attributeLengths;

        this.pairing = PairingFactory.getPairing(curveParams);
    }

    public void init(CurveParams curveParams, int n) {
        this.curveParams = curveParams;
        this.attributeLengths = new int[n];
        for (int i = 0; i < attributeLengths.length; i++) {
            attributeLengths[i] = 1;
        }

        this.pairing = PairingFactory.getPairing(curveParams);
    }


    public HVEIP08Parameters generateParameters() {
        Element g = pairing.getG1().newElement().setToRandom();

        return new HVEIP08Parameters(curveParams, g.getImmutable(), attributeLengths);
    }

}