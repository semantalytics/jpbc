package it.unisa.dia.gas.crypto.jpbc.fe.abe.gghvv13.params;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.util.ElementUtils;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class GGHVV13PublicKeyParameters extends GGHVV13KeyParameters {

    private final Element H;
    private final Element[] hs;

    public GGHVV13PublicKeyParameters(final GGHVV13Parameters parameters,
                                      final Element H,
                                      final Element[] hs) {
        super(false, parameters);

        this.H = H.getImmutable();
        this.hs = ElementUtils.cloneImmutable(hs);
    }

    public Element getH() {
        return H;
    }

    public Element getHAt(final int index) {
        return hs[index];
    }
}
