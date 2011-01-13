package it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.params;

import it.unisa.dia.gas.crypto.jpbc.utils.ElementUtil;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import org.bouncycastle.crypto.CipherParameters;

import java.util.Arrays;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class AHIBEPublicKeyParameters implements CipherParameters {

    private CurveParams curveParams;
    private Element Y1;
    private Element Y3;
    private Element Y4;
    private Element t;
    private Element[] us;
    private Element omega;


    public AHIBEPublicKeyParameters(CurveParams curveParams,
                                    Element y1, Element y3, Element y4,
                                    Element t, Element[] us,
                                    Element omega) {
        this.curveParams = curveParams;

        this.Y1 = y1.getImmutable();
        this.Y3 = y3.getImmutable();
        this.Y4 = y4.getImmutable();
        this.t = t.getImmutable();
        this.us = ElementUtil.cloneImmutably(us);
        this.omega = omega.getImmutable();
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

    public Element[] getUs() {
        return Arrays.copyOf(us, us.length);
    }

    public Element getOmega() {
        return omega;
    }

    public Element getUAt(int index) {
        return us[index];
    }

    public int getLength() {
        return us.length;
    }
}
