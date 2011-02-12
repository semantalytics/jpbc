package it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params;

import it.unisa.dia.gas.crypto.jpbc.utils.ElementUtil;
import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class IPLOSTW10PrivateKeyParameters extends IPLOSTW10KeyParameters {
    private Element[] Bstar;


    public IPLOSTW10PrivateKeyParameters(IPLOSTW10Parameters parameters, Element[] Bstar) {
        super(true, parameters);

        this.Bstar = ElementUtil.cloneImmutably(Bstar);
    }

    public Element getBStarAt(int index) {
        return Bstar[index];
    }

}