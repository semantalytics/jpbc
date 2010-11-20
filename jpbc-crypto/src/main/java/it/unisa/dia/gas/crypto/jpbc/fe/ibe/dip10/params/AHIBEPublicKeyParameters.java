package it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.params;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class AHIBEPublicKeyParameters implements CipherParameters {

    private CurveParams curveParams;
    private Element Y1;
    private Element Y3;
    private Element Y4;
    private Element t;
    private Element[] uElements;
    private Element omega;

    public AHIBEPublicKeyParameters(CurveParams curveParams,
                                    Element y1, Element y3, Element y4,
                                    Element t, Element[] uElements,
                                    Element omega) {
        this.curveParams = curveParams;
        Y1 = y1;
        Y3 = y3;
        Y4 = y4;
        this.t = t;
        this.uElements = uElements;
        this.omega = omega;
    }

    
    public CurveParams getCurveParams() {
        return curveParams;
    }

    public Element getY1() {
        return Y1;
    }

    public Element getY3() {
        return Y3;
    }

    public Element getY4() {
        return Y4;
    }

    public Element getT() {
        return t;
    }

    public Element[] getuElements() {
        return uElements;
    }

    public Element getOmega() {
        return omega;
    }
}
