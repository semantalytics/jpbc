package it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params;

import it.unisa.dia.gas.crypto.jpbc.utils.ElementUtil;
import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class IPOT10PrivateKeyParameters extends IPOT10KeyParameters {
    private Element[] Bstar;


    public IPOT10PrivateKeyParameters(IPOT10Parameters parameters, Element[] Bstar) {
        super(true, parameters);

        this.Bstar = ElementUtil.cloneImmutably(Bstar);
    }

    public Element getBStarAt(int index) {
        return Bstar[index];
    }

}