package it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.params;

import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.jpbc.Element;
import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UHIBEPublicKeyParameters implements CipherParameters {

    private CurveParameters curveParams;
    private Element g;
    private Element u;
    private Element h;
    private Element v;
    private Element w;
    private Element omega;


    public UHIBEPublicKeyParameters(CurveParameters curveParams,
                                    Element g, Element u, Element h,
                                    Element v, Element w,
                                    Element omega) {
        this.curveParams = curveParams;

        this.g = g.getImmutable();
        this.u = u.getImmutable();
        this.h = h.getImmutable();
        this.v = v.getImmutable();
        this.w = w.getImmutable();
        this.omega = omega.getImmutable();
    }

    
    public CurveParameters getCurveParams() {
        return curveParams;
    }

    public Element getG() {
        return g;
    }

    public Element getU() {
        return u;
    }

    public Element getH() {
        return h;
    }

    public Element getV() {
        return v;
    }

    public Element getW() {
        return w;
    }

    public Element getOmega() {
        return omega;
    }

}
