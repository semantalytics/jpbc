package it.unisa.dia.gas.crypto.jpbc.fe.ip.ot10.params;

import it.unisa.dia.gas.crypto.jpbc.utils.ElementUtil;
import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class IPOT10PublicKeyParameters extends IPOT10KeyParameters {
    private Element[] B;
    private Element sigma;


    public IPOT10PublicKeyParameters(IPOT10Parameters parameters, Element[] B, Element sigma) {
        super(false, parameters);

        this.B = ElementUtil.cloneImmutably(B);
        this.sigma = sigma.getImmutable();
    }


    public Element getBAt(int index) {
        return B[index];
    }

    public Element getSigma() {
        return sigma;
    }
}