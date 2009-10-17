package it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.strong.params;

import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMAStrongMasterSecretKeyParameters extends UTMAStrongKeyParameters {
    private Element t1, t2, t3, w;


    public UTMAStrongMasterSecretKeyParameters(UTMAStrongPublicParameters publicParameters,
                                               Element t1, Element t2, Element t3, Element w) {
        super(true, publicParameters);

        this.t1 = t1;
        this.t2 = t2;
        this.t3 = t3;
        this.w = w;
    }

    
    public Element getT1() {
        return t1;
    }

    public Element getT2() {
        return t2;
    }

    public Element getT3() {
        return t3;
    }

    public Element getW() {
        return w;
    }

}