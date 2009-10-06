package it.unisa.dia.gas.plaf.jpbc.pbc.jna;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCPairingPPType extends Memory {

    public PBCPairingPPType() {
        super(PBCLibraryProvider.getPbcLibrary().pbc_pairing_pp_sizeof());
    }


    public PBCPairingPPType(Pointer element, Pointer pairing) {
        this();
        PBCLibraryProvider.getPbcLibrary().pbc_pairing_pp_init(this, element, pairing);        
    }

    @Override
    protected void finalize() {
        PBCLibraryProvider.getPbcLibrary().pbc_pairing_pp_clear(this);
        
        super.finalize();
    }
}