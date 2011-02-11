package it.unisa.dia.gas.crypto.jpbc.signature.bls01.params;

import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.jpbc.Element;
import org.bouncycastle.crypto.CipherParameters;

import java.io.Serializable;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class BLS01Parameters implements CipherParameters, Serializable {
    private CurveParameters curveParams;
    private Element g;


    public BLS01Parameters(CurveParameters curveParams, Element g) {
        this.curveParams = curveParams;
        this.g = g;
    }


    public CurveParameters getCurveParams() {
        return curveParams;
    }

    public Element getG() {
        return g;
    }

}