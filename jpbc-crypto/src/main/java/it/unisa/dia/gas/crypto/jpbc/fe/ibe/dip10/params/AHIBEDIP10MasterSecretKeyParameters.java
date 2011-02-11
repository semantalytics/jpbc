package it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.params;

import it.unisa.dia.gas.jpbc.Element;
import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class AHIBEDIP10MasterSecretKeyParameters implements CipherParameters {

    private Element X1;
    private Element alpha;


    public AHIBEDIP10MasterSecretKeyParameters(Element x1, Element alpha) {
        this.X1 = x1.getImmutable();
        this.alpha = alpha.getImmutable();
    }

    public Element getX1() {
        return X1;
    }

    public Element getAlpha() {
        return alpha;
    }
}
