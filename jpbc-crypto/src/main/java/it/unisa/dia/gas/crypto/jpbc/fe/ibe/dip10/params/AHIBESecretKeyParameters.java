package it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.params;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class AHIBESecretKeyParameters implements CipherParameters {
    private CurveParams curveParams;
    private Element k11, k12;
    private Element k21, k22;
    private Element[] E1s, E2s;
    private Element[] ids;

    public AHIBESecretKeyParameters(CurveParams curveParams,
            Element k11, Element k12, Element[] e1s,
                                    Element k21, Element k22, Element[] e2s,
                                    Element[] ids) {
        this.curveParams = curveParams;
        this.k11 = k11;
        this.k12 = k12;
        this.E1s = e1s;
        this.k21 = k21;
        this.k22 = k22;
        this.E2s = e2s;
        this.ids = ids;
    }


    public CurveParams getCurveParams() {
        return curveParams;
    }

    public Element getK11() {
        return k11;
    }

    public Element getK12() {
        return k12;
    }

    public Element getK21() {
        return k21;
    }

    public Element getK22() {
        return k22;
    }

    public Element[] getE1s() {
        return E1s;
    }

    public Element[] getE2s() {
        return E2s;
    }

    public Element[] getIds() {
        return ids;
    }
}
