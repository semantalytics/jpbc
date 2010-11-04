package it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.weak.params;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import org.bouncycastle.crypto.CipherParameters;

import java.io.Serializable;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMAWeakPublicParameters implements CipherParameters, Serializable {
    private CurveParams curveParams;
    private Element g;
    private Element g0, g1;
    private Element omega;
    private Element T1, T2, T3;


    public UTMAWeakPublicParameters(CurveParams curveParams, Element g, Element g0, Element g1, Element omega, Element T1, Element T2, Element T3) {
        this.curveParams = curveParams;
        this.g = g;
        this.g0 = g0;
        this.g1 = g1;
        this.omega = omega;
        this.T1 = T1;
        this.T2 = T2;
        this.T3 = T3;
    }

    public CurveParams getCurveParams() {
        return curveParams;
    }

    public Element getG() {
        return g.duplicate();
    }

    public Element getG0() {
        return g0;
    }

    public Element getG1() {
        return g1.duplicate();
    }

    public Element getOmega() {
        return omega.duplicate();
    }

    public Element getT1() {
        return T1.duplicate();
    }

    public Element getT2() {
        return T2.duplicate();
    }

    public Element getT3() {
        return T3.duplicate();
    }
}
