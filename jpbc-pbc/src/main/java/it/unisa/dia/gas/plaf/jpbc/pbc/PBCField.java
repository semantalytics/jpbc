package it.unisa.dia.gas.plaf.jpbc.pbc;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.pbc.jna.PBCLibrary;
import it.unisa.dia.gas.plaf.jpbc.pbc.jna.PBCPairingType;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public abstract class PBCField implements Field {
    protected PBCPairingType pairing;
    protected int fixedLengthInBytes;


    protected PBCField(PBCPairingType pairing) {
        this.pairing = pairing;

        PBCElement element = (PBCElement) newElement();
        fixedLengthInBytes = PBCLibrary.INSTANCE.pbc_element_length_in_bytes(element.value);
    }
    

    public Element newZeroElement() {
        return newElement().setToZero();
    }

    public Element newOneElement() {
        return newElement().setToOne();
    }

    public BigInteger getOrder() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public Element getNqr() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public int getFixedLengthInBytes() {
        return fixedLengthInBytes;
    }

    public Element sub(Element e1, Element e2) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }
}
