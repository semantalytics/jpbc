package it.unisa.dia.gas.plaf.jpbc.wrapper.jna;

import com.sun.jna.Memory;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCElementType extends Memory {
    public enum FieldType{
        G1, G2, GT, Zr
    }

    public PBCElementType() {
        super(WrapperLibraryProvider.getWrapperLibrary().pbc_element_sizeof());
    }

    public PBCElementType(FieldType fieldType, PBCPairingType pairing) {
        super(WrapperLibraryProvider.getWrapperLibrary().pbc_element_sizeof());
        switch (fieldType) {
            case G1:
                WrapperLibraryProvider.getWrapperLibrary().pbc_element_init_G1(this, pairing);
                break;
            case G2:
                WrapperLibraryProvider.getWrapperLibrary().pbc_element_init_G2(this, pairing);
                break;
            case GT:
                WrapperLibraryProvider.getWrapperLibrary().pbc_element_init_GT(this, pairing);
                break;
            case Zr:
                WrapperLibraryProvider.getWrapperLibrary().pbc_element_init_Zr(this, pairing);
                break;
        }
    }


    @Override
    protected void finalize() {
        if (isValid()) {
            WrapperLibraryProvider.getWrapperLibrary().pbc_element_clear(this);

            super.finalize();
        }
    }
}
