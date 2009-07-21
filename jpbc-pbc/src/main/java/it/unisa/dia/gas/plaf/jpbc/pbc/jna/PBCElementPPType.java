package it.unisa.dia.gas.plaf.jpbc.pbc.jna;

import com.sun.jna.Memory;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCElementPPType extends Memory {

    public PBCElementPPType() {
        super(PBCLibraryProvider.getPbcLibrary().pbc_element_pp_sizeof());
    }

    @Override
    protected void finalize() {
        PBCLibraryProvider.getPbcLibrary().pbc_element_pp_clear(this);
        super.finalize();
    }
}