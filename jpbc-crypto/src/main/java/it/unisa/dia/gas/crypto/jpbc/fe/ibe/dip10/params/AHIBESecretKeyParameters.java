package it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.params;

import it.unisa.dia.gas.crypto.jpbc.utils.ElementUtil;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import org.bouncycastle.crypto.CipherParameters;

import java.util.Arrays;

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

        this.k11 = k11.getImmutable();
        this.k12 = k12.getImmutable();
        this.E1s = ElementUtil.cloneImmutably(e1s);
        this.k21 = k21.getImmutable();
        this.k22 = k22.getImmutable();
        this.E2s = ElementUtil.cloneImmutably(e2s);
        this.ids = ElementUtil.cloneImmutably(ids);
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
        return Arrays.copyOf(E1s, E1s.length);
    }

    public Element getE1At(int index) {
        return E1s[index];
    }

    public Element[] getE2s() {
        return Arrays.copyOf(E2s, E2s.length);
    }

    public Element getE2At(int index) {
        return E2s[index];
    }

    public Element[] getIds() {
        return Arrays.copyOf(ids, ids.length);
    }

    public int getE1Length() {
        return E1s.length;
    }
}
