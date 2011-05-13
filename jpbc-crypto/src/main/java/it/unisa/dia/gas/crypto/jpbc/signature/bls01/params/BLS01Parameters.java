package it.unisa.dia.gas.crypto.jpbc.signature.bls01.params;

import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.jpbc.Element;
import org.bouncycastle.crypto.CipherParameters;

import java.io.Serializable;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class BLS01Parameters implements CipherParameters, Serializable {
    private CurveParameters curveParameters;
    private Element g;


    public BLS01Parameters(CurveParameters curveParameters, Element g) {
        this.curveParameters = curveParameters;
        this.g = g;
    }


    public CurveParameters getCurveParameters() {
        return curveParameters;
    }

    public Element getG() {
        return g;
    }

}