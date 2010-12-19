package it.unisa.dia.gas.plaf.jpbc.wrapper.jna;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCPairingPPType extends Memory {

    public PBCPairingPPType() {
        super(WrapperLibraryProvider.getWrapperLibrary().pbc_pairing_pp_sizeof());
    }


    public PBCPairingPPType(Pointer element, Pointer pairing) {
        this();
        WrapperLibraryProvider.getWrapperLibrary().pbc_pairing_pp_init(this, element, pairing);
    }

    @Override
    protected void finalize() {
        if (isValid()) {
            WrapperLibraryProvider.getWrapperLibrary().pbc_pairing_pp_clear(this);
            peer = 0;
        }
    }
}