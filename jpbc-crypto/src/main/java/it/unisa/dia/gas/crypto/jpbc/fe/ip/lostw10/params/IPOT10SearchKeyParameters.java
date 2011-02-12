package it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params;

import it.unisa.dia.gas.jpbc.Element;

import java.util.List;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class IPOT10SearchKeyParameters extends IPOT10KeyParameters {
    private List<Element> Y, L;
    private Element K;


    public IPOT10SearchKeyParameters(IPOT10Parameters parameters, List<Element> Y, List<Element> L) {
        super(true, parameters);

        this.Y = Y;
        this.L = L;
        this.K = null;
    }

    public IPOT10SearchKeyParameters(IPOT10Parameters parameters, Element k) {
        super(true, parameters);
        K = k;
    }


    public List<Element> getY() {
        return Y;
    }

    public List<Element> getL() {
        return L;
    }

    public Element getK() {
        return K;
    }
}