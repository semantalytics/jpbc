package it.unisa.dia.gas.crypto.jpbc.rfid.utma.weak.params;

import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMAWeakPrivateKeyParameters extends UTMAWeakKeyParameters {
    private Element D0, D1, D2, D3;


    public UTMAWeakPrivateKeyParameters(UTMAWeakPublicParameters publicParameters, Element d0, Element d1, Element d2, Element d3) {
        super(true, publicParameters);

        D0 = d0;
        D1 = d1;
        D2 = d2;
        D3 = d3;
    }

    public Element getD0() {
        return D0;
    }

    public Element getD1() {
        return D1;
    }

    public Element getD2() {
        return D2;
    }

    public Element getD3() {
        return D3;
    }
}