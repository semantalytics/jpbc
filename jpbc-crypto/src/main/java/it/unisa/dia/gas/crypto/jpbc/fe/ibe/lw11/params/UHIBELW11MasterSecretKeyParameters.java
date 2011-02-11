package it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.params;

import it.unisa.dia.gas.jpbc.Element;
import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UHIBELW11MasterSecretKeyParameters implements CipherParameters {

    private Element alpha;


    public UHIBELW11MasterSecretKeyParameters(Element alpha) {
        this.alpha = alpha.getImmutable();
    }


    public Element getAlpha() {
        return alpha;
    }
}
