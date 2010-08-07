package it.unisa.dia.gas.plaf.jpbc.crypto.ahibe.params;

import it.unisa.dia.gas.jpbc.Element;
import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class AHIBESecretKeyParameters implements CipherParameters {
    private Element k1;
    private Element   k2;
    private Element[] Es;


    public AHIBESecretKeyParameters(Element k1, Element k2, Element[] es) {
        this.k1 = k1;
        this.k2 = k2;
        this.Es = es;
    }


    public Element getK1() {
        return k1;
    }

    public Element getK2() {
        return k2;
    }

    public Element[] getEs() {
        return Es;
    }
}
