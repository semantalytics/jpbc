package it.unisa.dia.gas.plaf.jpbc.pbc;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.pbc.jna.MPZElementType;
import it.unisa.dia.gas.plaf.jpbc.pbc.jna.PBCLibraryProvider;
import it.unisa.dia.gas.plaf.jpbc.pbc.jna.PBCPairingType;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public abstract class PBCField implements Field {
    protected PBCPairingType pairing;

    protected int fixedLengthInBytes;
    protected BigInteger order;


    protected PBCField(PBCPairingType pairing) {
        this.pairing = pairing;

        if (pairing != null) {
            PBCElement temp = (PBCElement) newElement();

            this.fixedLengthInBytes = PBCLibraryProvider.getPbcLibrary().pbc_element_length_in_bytes(temp.value);

            MPZElementType mpzOrder = new MPZElementType();
            mpzOrder.init();
            PBCLibraryProvider.getPbcLibrary().pbc_field_order(temp.value, mpzOrder);
            this.order = new BigInteger(mpzOrder.toString(10));
        }
    }
    

    public Element newZeroElement() {
        return newElement().setToZero();
    }

    public Element newOneElement() {
        return newElement().setToOne();
    }

    public Element newRandomElement() {
        return newElement().setToRandom();
    }

    public Element newElement(int value) {
        return newElement(value);
    }

    public Element newElement(BigInteger value) {
        return newElement(value);
    }

    public BigInteger getOrder() {
        return order;
    }

    public Element getNqr() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public int getLengthInBytes() {
        return fixedLengthInBytes;
    }

}
