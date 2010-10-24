package it.unisa.dia.gas.plaf.jpbc.pbc.jna;

import com.sun.jna.Memory;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCElementType extends Memory {
    public enum FieldType{
        G1, G2, GT, Zr
    }

    public PBCElementType() {
        super(PBCLibraryProvider.getPbcLibrary().pbc_element_sizeof());
    }

    public PBCElementType(FieldType fieldType, PBCPairingType pairing) {
        super(PBCLibraryProvider.getPbcLibrary().pbc_element_sizeof());
        switch (fieldType) {
            case G1:
                PBCLibraryProvider.getPbcLibrary().pbc_element_init_G1(this, pairing);
                break;
            case G2:
                PBCLibraryProvider.getPbcLibrary().pbc_element_init_G2(this, pairing);
                break;
            case GT:
                PBCLibraryProvider.getPbcLibrary().pbc_element_init_GT(this, pairing);
                break;
            case Zr:
                PBCLibraryProvider.getPbcLibrary().pbc_element_init_Zr(this, pairing);
                break;
        }
    }


    @Override
    protected void finalize() {
        PBCLibraryProvider.getPbcLibrary().pbc_element_clear(this);

        super.finalize();
    }
}
