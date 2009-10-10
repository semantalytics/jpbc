package it.unisa.dia.gas.plaf.jpbc.crypto.ehve.params;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.ssod.SearchKey;

import java.util.List;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HVESearchKeyParameters extends HVEKeyParameters implements SearchKey {
    private List<Element> Y, L;
    private Element K;


    public HVESearchKeyParameters(HVEParameters parameters, List<Element> Y, List<Element> L) {
        super(true, parameters);

        this.Y = Y;
        this.L = L;
        this.K = null;
    }

    public HVESearchKeyParameters(HVEParameters parameters, Element k) {
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