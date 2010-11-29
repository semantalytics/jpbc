package it.unisa.dia.gas.plaf.jpbc.pbc;

import com.sun.jna.Pointer;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.util.BigIntegerUtils;
import it.unisa.dia.gas.plaf.jpbc.wrapper.jna.MPZElementType;
import it.unisa.dia.gas.plaf.jpbc.wrapper.jna.PBCPairingType;
import it.unisa.dia.gas.plaf.jpbc.wrapper.jna.WrapperLibraryProvider;

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

            this.fixedLengthInBytes = WrapperLibraryProvider.getWrapperLibrary().pbc_element_length_in_bytes(temp.value);

            MPZElementType mpzOrder = new MPZElementType();
            mpzOrder.init();
            WrapperLibraryProvider.getWrapperLibrary().pbc_field_order(temp.value, mpzOrder);
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

    public boolean isOrderOdd() {
        return BigIntegerUtils.isOdd(order);
    }

    public Element getNqr() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public int getLengthInBytes() {
        return fixedLengthInBytes;
    }

    public Element[] twice(Element[] elements) {
        Pointer[] pointers = new Pointer[elements.length];
        for (int i = 0; i < elements.length; i++) {
            pointers[i] = ((PBCElement) elements[i]).getValue();
        }

        WrapperLibraryProvider.getWrapperLibrary().pbc_element_multi_double(pointers, pointers, elements.length);

        return elements;
    }

    public Element[] add(Element[] a, Element[] b) {
        Pointer[] aPointers = new Pointer[a.length];
        for (int i = 0; i <a.length; i++) {
            aPointers[i] = ((PBCElement) a[i]).getValue();
        }

        Pointer[] bPointers = new Pointer[b.length];
        for (int i = 0; i <b.length; i++) {
            bPointers[i] = ((PBCElement) b[i]).getValue();
        }

        WrapperLibraryProvider.getWrapperLibrary().pbc_element_multi_add(aPointers, aPointers, bPointers, a.length);

        return a;
    }
}
