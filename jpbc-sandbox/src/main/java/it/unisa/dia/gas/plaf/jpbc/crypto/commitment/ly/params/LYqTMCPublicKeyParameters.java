package it.unisa.dia.gas.plaf.jpbc.crypto.commitment.ly.params;

import it.unisa.dia.gas.jpbc.Element;

import java.util.List;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class LYqTMCPublicKeyParameters extends LYqTMCKeyParameters {
    private List<Element> pk;


    public LYqTMCPublicKeyParameters(LYqTMCParameters parameters, List<Element> pk) {
        super(false, parameters);
        this.pk = pk;
    }


    public List<Element> getPk() {
        return pk;
    }
}