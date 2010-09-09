package it.unisa.dia.gas.plaf.jpbc.crypto.ibe.dip10.params;

import it.unisa.dia.gas.jpbc.Element;
import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class AHIBEMasterSecretKeyParameters implements CipherParameters {

    private Element X1;
    private Element alpha;


    public AHIBEMasterSecretKeyParameters(Element x1, Element alpha) {
        X1 = x1;
        this.alpha = alpha;
    }

    public Element getX1() {
        return X1;
    }

    public Element getAlpha() {
        return alpha;
    }
}
