package it.unisa.dia.gas.plaf.jpbc.crypto.bls.params;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import org.bouncycastle.crypto.CipherParameters;

import java.io.Serializable;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class BLSParameters implements CipherParameters, Serializable {
    private CurveParams curveParams;
    private Element g;


    public BLSParameters(CurveParams curveParams, Element g) {
        this.curveParams = curveParams;
        this.g = g;
    }

    public CurveParams getCurveParams() {
        return curveParams;
    }

    public Element getG() {
        return g;
    }

}