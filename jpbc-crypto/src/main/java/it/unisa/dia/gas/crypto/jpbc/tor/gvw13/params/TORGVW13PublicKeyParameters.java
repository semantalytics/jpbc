package it.unisa.dia.gas.crypto.jpbc.tor.gvw13.params;

import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class TORGVW13PublicKeyParameters extends TORGVW13KeyParameters {

    private Element left;
    private Element right;
    private int level;

    public TORGVW13PublicKeyParameters(TORGVW13Parameters parameters, Element left, Element right, int level) {
        super(false, parameters);

        this.left = left;
        this.right = right;
        this.level = level;
    }

    public Element getLeft() {
        return left;
    }

    public Element getRight() {
        return right;
    }

    public int getLevel() {
        return level;
    }
}
