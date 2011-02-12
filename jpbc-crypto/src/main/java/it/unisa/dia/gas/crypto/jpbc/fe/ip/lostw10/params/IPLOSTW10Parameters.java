package it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params;

import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.jpbc.Element;
import org.bouncycastle.crypto.CipherParameters;

import java.io.Serializable;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class IPLOSTW10Parameters implements CipherParameters, Serializable {
    private CurveParameters curveParams;
    private Element g;
    private int n;


    public IPLOSTW10Parameters(CurveParameters curveParams, Element g, int n) {
        this.curveParams = curveParams;
        this.g = g.getImmutable();
        this.n = n;
    }


    public CurveParameters getCurveParams() {
        return curveParams;
    }

    public Element getG() {
        return g;
    }

    public int getN() {
        return n;
    }


}