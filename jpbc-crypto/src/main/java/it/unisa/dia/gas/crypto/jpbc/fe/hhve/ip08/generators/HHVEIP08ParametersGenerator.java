package it.unisa.dia.gas.crypto.jpbc.fe.hhve.ip08.generators;

import it.unisa.dia.gas.crypto.jpbc.fe.hhve.ip08.params.HHVEIP08Parameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.util.Arrays;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HHVEIP08ParametersGenerator {
    private CurveParams curveParams;
    private int[] attributeLengths;

    private Pairing pairing;


    public void init(CurveParams curveParams, int... attributeLengths) {
        this.curveParams = curveParams;
        this.attributeLengths = Arrays.copyOf(attributeLengths, attributeLengths.length);

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


    public HHVEIP08Parameters generateParameters() {
        Element g = pairing.getG1().newElement().setToRandom();

        return new HHVEIP08Parameters(curveParams, g.getImmutable(), attributeLengths);
    }

}