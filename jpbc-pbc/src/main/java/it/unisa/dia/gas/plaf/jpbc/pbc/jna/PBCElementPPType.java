package it.unisa.dia.gas.plaf.jpbc.pbc.jna;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCElementPPType extends Memory {

    public PBCElementPPType() {
        super(PBCLibraryProvider.getPbcLibrary().pbc_element_pp_sizeof());
    }

    public PBCElementPPType(Pointer element) {
        this();
        
        PBCLibraryProvider.getPbcLibrary().pbc_element_pp_init(this, element);
    }

    @Override
    protected void finalize() {
        System.out.println("PBCElementPPType.finalize");
        PBCLibraryProvider.getPbcLibrary().pbc_element_pp_clear(this);
        super.finalize();
    }
}